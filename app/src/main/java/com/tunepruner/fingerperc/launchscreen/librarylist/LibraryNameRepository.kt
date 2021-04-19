package com.tunepruner.fingerperc.launchscreen.librarylist

import android.app.Application
import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.android.billingclient.api.*

class LibraryNameRepository(val app: Application) {
    val libraryNameData = MutableLiveData<List<LibraryDetails>>()
    val db = Firebase.firestore
    private val tag = "LibraryNameRepository.Class"
    private lateinit var purchasesUpdatedListener: PurchasesUpdatedListener
    private lateinit var billingClientWrapper: BillingClientWrapper


    init {
        getData()
    }

    private fun getData() {
        getLibraryNamesFromFirebase()
    }

    private fun getLibraryNamesFromFirebase() {
        val collectionRef = db.collection("libraries")
        val libraryDetails: ArrayList<LibraryDetails> = ArrayList()

        collectionRef.get()
            .addOnSuccessListener {
                if (it != null) {
                    val results: List<LibraryDetails> = it.toObjects(LibraryDetails::class.java)
                    for (element in results) {
                        libraryDetails.add(element)
                    }
                    for (element in libraryDetails) {
                        Log.i(tag, "${element.libraryName}")
                    }

                    updateInstallStatuses(libraryDetails)
                    updatePurchaseStatuses(libraryDetails)

                    val libraryNamesSorted = sortByStatus(libraryDetails)

                    libraryNameData.value = libraryNamesSorted
                } else {
                    Log.d(tag, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(tag, "get failed with ", exception)
            }

    }

    private fun updateInstallStatuses(libraryDetails: ArrayList<LibraryDetails>) {
        val assetManager: AssetManager = app.assets
        val filePaths = assetManager.list("audio/")
            ?: error("AssetManager couldn't get filePaths")
        for (i in 0..(libraryDetails.lastIndex)) {
            for (j in 0..filePaths.lastIndex) {
                val libraryName = libraryDetails[i].libraryID.toString()
                if (filePaths[j].contains(libraryName)) {
                    libraryDetails[i].isInstalled = true
                    break
                }
            }
            if (libraryDetails[i].isInstalled == null) libraryDetails[i].isInstalled = false
        }
    }

    fun updatePurchaseStatuses(libraryDetails: ArrayList<LibraryDetails>) {
        for (element in libraryDetails) {
            if (element.isPurchased == null && element.soundpackID != null){
                billingClientWrapper = BillingClientWrapper.getInstance(app.applicationContext, element.soundpackID)
                billingClientWrapper.checkIfPurchased()
            }
        }
    }

    fun sortByStatus(libraryDetails: ArrayList<LibraryDetails>): ArrayList<LibraryDetails> {
//        val unsorted = ArrayList<LibraryDetails>()
        val sorted = ArrayList<LibraryDetails>()
//        for (element in libraryDetails) {
//            unsorted.add(element)
//        }
        for (element in libraryDetails) {
            sorted.add(element)
        }
        sorted.sortWith(compareByDescending { it.isPurchased })
        return sorted
    }

}

data class LibraryDetails(
    val index: Int? = null,
    val libraryName: String? = null,
    val imageUrl: String? = null,
    @field:JvmField
    val isPurchased: Boolean? = null,
    @field:JvmField
    var isInstalled: Boolean? = null,
    @field:JvmField
    var isReleased: Boolean? = null,
    val libraryID: String? = null,
    val soundpackID: String? = null
    )
//changing this class might ruin firebase connection. Needs no-arguments constructor. But order of arguments doesn't matter