package com.tunepruner.fingerperc.launchscreen.librarylist

import kotlin.collections.ArrayList
import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.android.billingclient.api.*
import com.tunepruner.fingerperc.launchscreen.librarydetail.Soundbank
import com.tunepruner.fingerperc.launchscreen.librarydetail.Library
import com.tunepruner.fingerperc.launchscreen.librarydetail.Soundpack
import java.io.File

class SoundbankRepo(val app: Application, val soundpackID: String) : BillingClientListener {
    private lateinit var soundbankPrimitive: Soundbank
    val soundbank = MutableLiveData<Soundbank>()
    var libraries = ArrayList<Library>()
    val soundpacks = ArrayList<Soundpack>()
    private val db = Firebase.firestore
    private lateinit var billingClientWrapper: BillingClientWrapper
    private val TAG = "Repo.Class"

    init {
        populateFromFirestore("libraries")
    }

    companion object {

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun populateFromFirestore(collectionName: String) {
        val collectionRef = db.collection(collectionName)

        collectionRef.get()
            .addOnSuccessListener { it ->
                if (it != null) {
                    if (collectionName.contains("libraries")) {

                        val results: List<Library> = it.toObjects(Library::class.java)
                        for (element in results) {
                            libraries.add(element)
                        }
                        populateFromFirestore("soundpacks")

                    } else {
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

                        libraries = filterSoundpacks(libraries)
                        libraries.sortWith(compareByDescending { it.isPurchased })
                        soundbankPrimitive = Soundbank(libraries, soundpacks)
                        updateInstallStatuses(soundbankPrimitive)
                        soundbank.value = soundbankPrimitive
                        billingClientWrapper =
                            BillingClientWrapper.getInstance(this, app.applicationContext)

                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    override fun onClientReady() {
        billingClientWrapper.queryPurchases()
    }

    override fun onPurchasesQueried(soundpacksPurchased: ArrayList<Purchase>) {
        updatePurchaseStatuses(soundpacksPurchased)
    }

    private fun updateInstallStatuses(soundbankPrimitive: Soundbank) {
        val assetManager: AssetManager = app.assets
        val filePaths = assetManager.list("audio")
            ?: error("AssetManager couldn't get filePaths")
        for (i in 0..(soundbankPrimitive.libraries.lastIndex)) {
            for (j in 0..filePaths.lastIndex) {
                val libraryName = soundbankPrimitive.libraries[i].libraryID.toString()
                if (filePaths[j].contains(libraryName)) {
//                    soundbankPrimitive.libraries[i].isInstalled = true
//                    val testing = soundbank.value
                    soundbankPrimitive.set(Soundbank.SetType.IS_INSTALLED, true, soundbankPrimitive.libraries[i])
                    break
                }
            }
            if (soundbankPrimitive.libraries[i].isInstalled == null) {
                soundbankPrimitive.libraries[i].isInstalled = false
                Log.i(TAG, "${soundbankPrimitive.libraries[i].libraryName} is not installed...")

            }
        }

    }

    private fun updatePurchaseStatuses(listOfPurchases: ArrayList<Purchase>) {
        for (library in soundbankPrimitive.libraries) {
            if (soundbank.value?.check(Soundbank.CheckType.IS_PURCHASED, library) == true &&
                library.soundpackID != null
            ) {
                for (purchase in listOfPurchases) {
                    if (purchase.sku == library.soundpackID) {
                        soundbank.value?.set(Soundbank.SetType.IS_PURCHASED, true, library)
                    }
                }
            }
        }

    }

    private fun filterSoundpacks(list: ArrayList<Library>): ArrayList<Library> {
        val listToReturn = ArrayList<Library>()
        for (element in list) listToReturn.add(element)

        if (soundpackID.isNotEmpty()) {
            for (element in list) {
                element.soundpackID?.let { library ->
                    if (!library.contains(soundpackID)) {
                        listToReturn.remove(element)
                    }
                }
            }
        }
        return listToReturn
    }

    fun isBeta() {

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
                            file.writeText(isBeta.toString(), Charsets.UTF_8) //Todo double check the toString method to make sure it creates a simple "true" or "false"
                            break@loop
                        }
                    }
                }
        }

    }

    data class BetaLaunch(
        val versionNumber: Int? = null,
        val isCurrent: Boolean? = null
    )
}


