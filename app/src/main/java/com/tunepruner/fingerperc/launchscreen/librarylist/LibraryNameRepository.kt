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

class LibraryNameRepository(val app: Application, val soundpackID: String) : BillingClientListener {
    private var libraryListPrimitive = ArrayList<LibraryDetails>()
    val libraryListLiveData = MutableLiveData<ArrayList<LibraryDetails>>()
    private var soundpackListPrimitive = ArrayList<SoundpackDetails>()
    val soundpackListLiveData = MutableLiveData<ArrayList<SoundpackDetails>>()
    private val db = Firebase.firestore
    private lateinit var billingClientWrapper: BillingClientWrapper
    private val TAG = "Repo.Class"

    init {
        populateFromFirestore("libraries")
        populateFromFirestore("soundpacks")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun populateFromFirestore(collectionName: String) {
        val collectionRef = db.collection(collectionName)
        collectionRef.get()
            .addOnSuccessListener { it ->
                if (it != null) {
                    if (collectionName.contains("libraries")) {
                        val results: List<LibraryDetails> = it.toObjects(LibraryDetails::class.java)
                        for (element in results) {
                            libraryListPrimitive.add(element)
                        }
                        var filtered = filterSoundpacks(libraryListPrimitive)
                        Log.i(TAG, "populateFromFirestore: ${filtered.size}")
                        filtered.sortWith(compareByDescending { library -> library.isPurchased })
                        Log.i(TAG, "populateFromFirestore: ${filtered.size}")
                        filtered = updateInstallStatuses(filtered)
                        Log.i(TAG, "populateFromFirestore: ${filtered.size}")
                        billingClientWrapper =
                            BillingClientWrapper.getInstance(this, app.applicationContext)
                        libraryListLiveData.value = filtered
                        soundpackListLiveData.value = soundpackListPrimitive
                    } else {
                        val results: List<SoundpackDetails> =
                            it.toObjects(SoundpackDetails::class.java)
                        for (element in results)
                            soundpackListPrimitive.add(element)
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

    private fun updateInstallStatuses(list: ArrayList<LibraryDetails>): ArrayList<LibraryDetails> {
        val listToReturn = ArrayList<LibraryDetails>()

        val assetManager: AssetManager = app.assets
        val filePaths = assetManager.list("audio")
            ?: error("AssetManager couldn't get filePaths")
        for (i in 0..(list.lastIndex)) {
            for (j in 0..filePaths.lastIndex) {
                val libraryName = list[i].libraryID.toString()
//                Log.i(TAG, filePaths[j])
                if (filePaths[j].contains(libraryName)) {
                    list[i].isInstalled = true
                    break
                }
            }
            if (list[i].isInstalled == null) {
                list[i].isInstalled = false
                Log.i(TAG, "${list[i].libraryName} is not installed...")

            }
        }

        return list
    }

    private fun updatePurchaseStatuses(listOfPurchases: ArrayList<Purchase>) {
        var counter1 = 1
        for (libraryDetails in libraryListPrimitive) {
//            Log.i(TAG, "libraryListPrimitive item # ${counter1++} = ${libraryDetails}: ")
            if (libraryDetails.isPurchased == null &&
                libraryDetails.soundpackID != null
            ) {
                var counter = 1
                for (purchase in listOfPurchases) {
//                    Log.i(TAG, "listOfPurchases item # ${counter++} = ${purchase.sku}: ")
                    if (purchase.sku == libraryDetails.soundpackID) {
//                        Log.i(TAG, "starting library details: ")
                        libraryDetails.isPurchased = true
//                        Log.i(TAG, "changing ${purchase.sku} to true: ")
                    } else {
//                        Log.i(TAG, "${purchase.sku} doesn't equal ${libraryDetails.soundpackID}: ")
                    }
                }
            }
        }
    }

    private fun filterSoundpacks(list: ArrayList<LibraryDetails>): ArrayList<LibraryDetails> {
        val listToReturn = ArrayList<LibraryDetails>()
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


data class LibraryDetails(
    val index: Int? = null,
    val libraryName: String? = null,
    val imageUrl: String? = null,
    @field:JvmField
    var isPurchased: Boolean? = null,
    @field:JvmField
    var isInstalled: Boolean? = null,
    @field:JvmField
    var isReleased: Boolean? = null,
    val libraryID: String? = null,
    val soundpackID: String? = null,
    val soundpackName: String? = null
)
//changing this class might ruin firebase connection. Needs no-arguments constructor. But order of arguments doesn't matter

data class SoundpackDetails(
    val soundpackID: String? = null,
    val soundpackName: String? = null,
    val index: Int? = null,
    val thumbnailImageUrl: String? = null,
    val titleImageUrl: String? = null,
    @field:JvmField
    var isPurchased: Boolean? = null,
    @field:JvmField
    var isInstalled: Boolean? = null,
    @field:JvmField
    var isReleased: Boolean? = null,
    val members: List<LibraryDetails>? = null,
)
