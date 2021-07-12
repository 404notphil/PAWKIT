package com.tunepruner.fingerperc.launchscreen.data

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tunepruner.fingerperc.launchscreen.librarydetail.Library
import com.tunepruner.fingerperc.launchscreen.librarydetail.Soundbank
import com.tunepruner.fingerperc.launchscreen.librarydetail.Soundpack
import com.tunepruner.fingerperc.launchscreen.librarylist.BillingClientListener
import com.tunepruner.fingerperc.launchscreen.librarylist.BillingClientWrapper
import java.io.File

class SoundbankRepo(val app: Application, val soundpackID: String) : BillingClientListener {
    private lateinit var soundbankPrimitive: Soundbank
    val soundbankLiveData = MutableLiveData<Soundbank>()
    var libraries = ArrayList<Library>()
    val soundpacks = ArrayList<Soundpack>()
    private val db = Firebase.firestore
    private lateinit var billingClientWrapper: BillingClientWrapper
    private val LOG_TAG = "Repo.Class"

    @RequiresApi(Build.VERSION_CODES.N)
    fun getCollectionFromFirestore() {
        Log.d(LOG_TAG, "getCollectionFromFirestore() called!")
        isBeta()
        val librariesCollectionRef = db.collection("libraries")
        val soundpacksCollectionRef = db.collection("soundpacks")

        librariesCollectionRef.get()
            .addOnSuccessListener { it ->
                Log.i(LOG_TAG, "onSuccessCodeRun")
                if (it != null) {
                    Log.i(LOG_TAG, "libraries listener code was run!")

                    val results: List<Library> = it.toObjects(Library::class.java)
                    for (element in results) {
                        libraries.add(element)
                    }

                    //getting soundpacks from firebase ONLY AFTER libraries.
                    //This way, I can ensure that I have BOTH before initializing
                    //soundbankPrimitive.
                    soundpacksCollectionRef.get()
                        .addOnSuccessListener {
                            Log.i(LOG_TAG, "soundpacks listener code was run!")
                            val results: List<Soundpack> =
                                it.toObjects(Soundpack::class.java)
                            for (element in results)
                                soundpacks.add(element)
                            for (soundpack in soundpacks) {
                                for (library in libraries) {
                                    if (library.soundpackID == soundpack.soundpackID) {
                                        soundpack.members.add(library)
                                    }
                                }
                            }
                            libraries =
                                filterSoundpacks(libraries)/*This only does something if this class holds a value in soundpackID*/
                            libraries.sortWith(compareByDescending { it.isPurchased })

                            soundbankPrimitive = Soundbank(libraries, soundpacks)
                            updateInstallStatuses(soundbankPrimitive)
                            billingClientWrapper =
                                BillingClientWrapper.getInstance(this, app.applicationContext)
                        }
                        .addOnFailureListener { exception ->
                            Log.d(LOG_TAG, "get failed with ", exception)
                        }


                } else {
                    Log.d(LOG_TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(LOG_TAG, "get failed with ", exception)
            }
        Log.d(LOG_TAG, "listener evaluated for librariesCollectionRef, will run shortly...");


        Log.d(LOG_TAG, "listener evaluated for soundpacksCollectionRef, will run shortly...");
    }

    override fun onClientReady() {
//        Log.d(LOG_TAG, "onClientReady() called")
        billingClientWrapper.queryPurchases()
    }

    override fun onPurchasesQueried(soundpacksPurchased: ArrayList<Purchase>) {
        updatePurchaseStatuses(soundpacksPurchased)
    }

    private fun updateInstallStatuses(soundbankPrimitive: Soundbank) {
//        Log.d(
//            LOG_TAG,
//            "updateInstallStatuses() called with: soundbankPrimitive = $soundbankPrimitive"
//        )
        val assetManager: AssetManager = app.assets
        val filePaths = assetManager.list("audio")
            ?: error("AssetManager couldn't get filePaths")
        for (i in 0..(soundbankPrimitive.libraries.lastIndex)) {
            for (j in 0..filePaths.lastIndex) {
                val libraryName = soundbankPrimitive.libraries[i].libraryID.toString()
                if (filePaths[j].contains(libraryName)) {
//                    soundbankPrimitive.libraries[i].isInstalled = true
//                    val testing = soundbank.value
                    soundbankPrimitive.set(
                        Soundbank.SetType.IS_INSTALLED,
                        true,
                        soundbankPrimitive.libraries[i]
                    )
                    break
                }
            }
            if (soundbankPrimitive.libraries[i].isInstalled == null) {
                soundbankPrimitive.libraries[i].isInstalled = false
            }
        }
    }

    private fun updatePurchaseStatuses(listOfPurchases: ArrayList<Purchase>) {
        for (library in soundbankPrimitive.libraries) {
            if (soundbankPrimitive.check(
                    Soundbank.CheckType.IS_PURCHASED,
                    library
                ) && library.soundpackID != null
            ) {
                for (purchase in listOfPurchases) {
                    if (purchase.sku == library.soundpackID) {
                        soundbankPrimitive.set(Soundbank.SetType.IS_PURCHASED, true, library)
                    }
                }
            }
        }
        soundbankLiveData.value = soundbankPrimitive

        //Log the purchase statuses
        Log.i(LOG_TAG, "_______________")
        for (element in soundbankPrimitive.libraries) {
            Log.i(LOG_TAG, "${element.soundpackName} is purchase? ${element.isPurchased}")
        }
        Log.i(LOG_TAG, "_______________")

    }

    private fun filterSoundpacks(list: ArrayList<Library>): ArrayList<Library> {
//        Log.d(LOG_TAG, "filterSoundpacks() called with: list = $list")
        /*since this class is used for both the main LibraryListRecyclerFragment and the SoundpackDetailFragment,
        it has a field called "soundpackID" that stores an empty string if this class is serving
        LibraryListRecyclerFragment and otherwise it contains the name of the soundpack it is
        displaying, in which case the code below is for removing all libraries that don't belong to that
        soundpack.
        */
        val listToReturn = ArrayList<Library>()
        for (element in list) listToReturn.add(element)

        if (soundpackID.isNotEmpty()) {
            for (element in list) {
                Log.i(LOG_TAG, "listToReturn: ${listToReturn.size}")
                element.soundpackID?.let { library ->
                    if (!library.contains(soundpackID)) {
                        listToReturn.remove(element)
                    }
                }
            }
        }
        return listToReturn
    }

    private fun isBeta() {
        Log.d(LOG_TAG, "isBeta() called")

        //first, get the persisted version of the story.
        val file = File(app.filesDir, "is_beta")
        val textFromFile: String = if (file.exists()) {
            file.readText()
        } else "null"

        var isBeta = when {/*this will potentially be changed by the database check*/
            textFromFile.contains("true") -> true
            textFromFile.contains("false") -> false
            else -> null
        }

        if (isBeta != false) {
            var verCode: Int? = null
            try {
                val pInfo: PackageInfo =
                    app.applicationContext.packageManager.getPackageInfo(
                        app.applicationContext.packageName,
                        0
                    )
                verCode = pInfo.versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            //next see if the remote database agrees with the persisted story (remote takes precedence)
            val collectionRef = db.collection("betaLaunches")

            collectionRef.get()
                .addOnSuccessListener {
                    val results: List<BetaLaunch> = it.toObjects(BetaLaunch::class.java)
                    loop@ for (element in results) {
                        if (element.versionNumber == verCode) {
                            isBeta = element.isCurrent
                            file.writeText(
                                isBeta.toString(),
                                Charsets.UTF_8
                            ) //Todo double check the toString method to make sure it creates a simple "true" or "false"
                            break@loop
                        }
                    }
                }
        }


    }

    data class BetaLaunch(
        val versionNumber: Int? = null,
        @field:JvmField
        val isCurrent: Boolean? = null
    )
}


