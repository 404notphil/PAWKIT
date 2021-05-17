package com.tunepruner.fingerperc.launchscreen.librarylist

import kotlin.collections.ArrayList
import android.app.Application
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
}


