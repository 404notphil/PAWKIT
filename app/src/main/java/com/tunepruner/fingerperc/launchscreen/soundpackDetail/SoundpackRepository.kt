package com.tunepruner.fingerperc.launchscreen.soundpackDetail

import com.tunepruner.fingerperc.launchscreen.librarylist.BillingClientListener
import com.tunepruner.fingerperc.launchscreen.librarylist.BillingClientWrapper


import android.app.Application
import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.android.billingclient.api.*

class SoundpackRepository(val app: Application) : BillingClientListener {
    val soundpackListLiveData = MutableLiveData<List<SoundpackDetails>>()
    var soundpackListPrimitive = ArrayList<SoundpackDetails>()
    private val db = Firebase.firestore
    private val TAG = "SoundpackRepo.Class"
    private lateinit var billingClientWrapper: BillingClientWrapper

    init {
        getData()
    }

    private fun getData() {
        getSoundpackNamesFromFirebase()
    }

    private fun getSoundpackNamesFromFirebase() {
        val collectionRef = db.collection("soundpacks")
        val soundpackDetails: ArrayList<SoundpackDetails> = ArrayList()

        collectionRef.get()
            .addOnSuccessListener {
                if (it != null) {
                    val results: List<SoundpackDetails> = it.toObjects(SoundpackDetails::class.java)
                    for (element in results) {
                        soundpackDetails.add(element)
                    }

                    val soundpackDetailsListSorted = sortByStatus(soundpackDetails)
                    val soundpackDetailsListWithInstalledStatus =
                        updateInstallStatuses(soundpackDetailsListSorted)
                    soundpackListPrimitive = soundpackDetailsListWithInstalledStatus
                    billingClientWrapper =
                        BillingClientWrapper.getInstance(this, app.applicationContext)

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
        soundpackListLiveData.value = soundpackListPrimitive
    }

    private fun updateInstallStatuses(soundpackDetails: ArrayList<SoundpackDetails>): ArrayList<SoundpackDetails> {
        val assetManager: AssetManager = app.assets
        val filePaths = assetManager.list("audio")
            ?: error("AssetManager couldn't get filePaths")
        for (i in 0..(soundpackDetails.lastIndex)) {
            for (j in 0..filePaths.lastIndex) {
                val soundpackName = soundpackDetails[i].soundpackID.toString()
                Log.i(TAG, filePaths[j])
                if (filePaths[j].contains(soundpackName)) {
                    soundpackDetails[i].isInstalled = true
                    Log.i(TAG, "${soundpackDetails[i].soundpackName} is installed!")
                    break
                }
            }
            if (soundpackDetails[i].isInstalled == null) {
                soundpackDetails[i].isInstalled = false
                Log.i(TAG, "${soundpackDetails[i].soundpackName} is not installed...")

            }
        }
        return soundpackDetails
    }

    private fun updatePurchaseStatuses(listOfPurchases: ArrayList<Purchase>) {
        for (soundpackDetails in soundpackListPrimitive) {
//            Log.i(TAG, "soundpackListPrimitive item # ${counter1++} = ${soundpackDetails}: ")
            if (soundpackDetails.isPurchased == null &&
                soundpackDetails.soundpackID != null
            ) {
                for (purchase in listOfPurchases) {
//                    Log.i(TAG, "listOfPurchases item # ${counter++} = ${purchase.sku}: ")
                    if (purchase.sku == soundpackDetails.soundpackID) {
//                        Log.i(TAG, "starting soundpack details: ")
                        soundpackDetails.isPurchased = true
//                        Log.i(TAG, "changing ${purchase.sku} to true: ")
                    }else{
//                        Log.i(TAG, "${purchase.sku} doesn't equal ${soundpackDetails.soundpackID}: ")
                    }
                }
            }
        }
    }
}

private fun sortByStatus(soundpackDetails: ArrayList<SoundpackDetails>): ArrayList<SoundpackDetails> {
//        val unsorted = ArrayList<SoundpackDetails>()
    val sorted = ArrayList<SoundpackDetails>()
//        for (element in soundpackDetails) {
//            unsorted.add(element)
//        }
    for (element in soundpackDetails) {
        sorted.add(element)
    }
    sorted.sortWith(compareByDescending { it.isPurchased })
    return sorted
}


data class SoundpackDetails(
    val soundpackName: String? = null,
    val imageUrl: String? = null,
    @field:JvmField
    var isPurchased: Boolean? = null,
    @field:JvmField
    var isInstalled: Boolean? = null,
    @field:JvmField
    var isReleased: Boolean? = null,
    val soundpackID: String? = null,
)
//changing this class might ruin firebase connection. Needs no-arguments constructor. But order of arguments doesn't matter