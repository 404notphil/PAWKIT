package com.tunepruner.fingerperc.launchscreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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
                } else {
                    Log.d(tag, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(tag, "get failed with ", exception)
            }
    }

}

data class LibraryName(
    val imageUrl: String? = null,
    val index: Int? = null,
    val libraryName: String? = null,
    @field:JvmField
    val isPurchased: Boolean? = null
    )
