package com.tunepruner.fingerperc.launchscreen.librarylist

import android.app.Application
import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.selects.select

class LibraryNameRepository(val app: Application) {
    val libraryNameData = MutableLiveData<List<LibraryName>>()
    val db = Firebase.firestore
    private val tag = "LibraryNameRepository.Class"


    init {
        getData()
    }

    private fun getData() {
        getLibraryNamesFromFirebase()
    }

    private fun getLibraryNamesFromFirebase() {
        val collectionRef = db.collection("libraries")
        val libraryNames: ArrayList<LibraryName> = ArrayList()

        collectionRef.get()
            .addOnSuccessListener {
                if (it != null) {
                    val results: List<LibraryName> = it.toObjects(LibraryName::class.java)
                    for (element in results) {
                        libraryNames.add(element)
                    }
                    for (element in libraryNames) {
                        Log.i(tag, "${element.libraryName}")
                    }

                    updateInstallStatuses(libraryNames)
//                    checkPurchases(libraryNames)


                    val libraryNamesSorted = sortByStatus(libraryNames)

                    libraryNameData.value = libraryNamesSorted
                } else {
                    Log.d(tag, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(tag, "get failed with ", exception)
            }

    }

    private fun updateInstallStatuses(libraryNames: ArrayList<LibraryName>) {
        val assetManager: AssetManager = app.assets
        val filePaths = assetManager.list("audio/")
            ?: error("AssetManager couldn't get filePaths")
        for (i in 0..(libraryNames.lastIndex)) {
            for (j in 0..filePaths.lastIndex) {
                val libraryName = libraryNames[i].libraryID.toString()
                if (filePaths[j].contains(libraryName)) {
                    libraryNames[i].isInstalled = true
                    break
                }
            }
            if (libraryNames[i].isInstalled == null) libraryNames[i].isInstalled = false
        }
    }

    fun updatePurchaseStatuses(libraryNames: ArrayList<LibraryName>) {

    }

    fun sortByStatus(libraryNames: ArrayList<LibraryName>): ArrayList<LibraryName> {
//        val unsorted = ArrayList<LibraryName>()
        val sorted = ArrayList<LibraryName>()
//        for (element in libraryNames) {
//            unsorted.add(element)
//        }
        for (element in libraryNames) {
            sorted.add(element)
        }
        sorted.sortWith (compareByDescending { it.isPurchased })
        return sorted
    }

}

data class LibraryName(
    val index: Int? = null,
    val libraryName: String? = null,
    val imageUrl: String? = null,
    @field:JvmField
    val isPurchased: Boolean? = null,
    @field:JvmField
    var isInstalled: Boolean? = null,
    @field:JvmField
    var isReleased: Boolean? = null,
    val libraryID: String? = null
)
//changing this class might ruin firebase connection. Needs no-arguments constructor. But order of arguments doesn't matter