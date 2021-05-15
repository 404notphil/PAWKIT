package com.tunepruner.fingerperc.launchscreen.librarylist

import android.app.Application
import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.android.billingclient.api.*
import com.google.firebase.firestore.CollectionReference

class LibraryNameRepository(val app: Application) : BillingClientListener {
    private var libraryListPrimitive = ArrayList<LibraryDetails>()
    val libraryListLiveData = MutableLiveData<List<LibraryDetails>>()
    private var soundpackListPrimitive = ArrayList<SoundpackDetails>()
    val soundpackListLiveData = MutableLiveData<List<SoundpackDetails>>()
    private val db = Firebase.firestore
    private lateinit var billingClientWrapper: BillingClientWrapper
    private val TAG = "Repo.Class"

    init {
        populateFromFirestore("libraries")
        populateFromFirestore("soundpacks")
    }

    private fun populateFromFirestore(collectionName: String) {
        val collectionRef = db.collection(collectionName)
        collectionRef.get()
            .addOnSuccessListener { it ->
                if (it != null) {
                    if (collectionName.contains("libraries")) {
                        val results: List<LibraryDetails> = it.toObjects(LibraryDetails::class.java)
                        for (element in results){
                            libraryListPrimitive.add(element)
                        }
                        libraryListPrimitive.sortWith(compareByDescending { library -> library.isPurchased })
                        updateInstallStatuses()
                        billingClientWrapper =
                            BillingClientWrapper.getInstance(this, app.applicationContext)
                        libraryListLiveData.value = libraryListPrimitive
                        soundpackListLiveData.value = soundpackListPrimitive
                    } else {
                        val results: List<SoundpackDetails> = it.toObjects(SoundpackDetails::class.java)
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

    private fun updateInstallStatuses(){
        val assetManager: AssetManager = app.assets
        val filePaths = assetManager.list("audio")
            ?: error("AssetManager couldn't get filePaths")
        for (i in 0..(libraryListPrimitive.lastIndex)) {
            for (j in 0..filePaths.lastIndex) {
                val libraryName = libraryListPrimitive[i].libraryID.toString()
//                Log.i(TAG, filePaths[j])
                if (filePaths[j].contains(libraryName)) {
                    libraryListPrimitive[i].isInstalled = true
//                    Log.i(TAG, "${libraryListPrimitive[i].libraryName} is installed!")
                    break
                }
            }
            if (libraryListPrimitive[i].isInstalled == null) {
                libraryListPrimitive[i].isInstalled = false
                Log.i(TAG, "${libraryListPrimitive[i].libraryName} is not installed...")

            }
        }
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
