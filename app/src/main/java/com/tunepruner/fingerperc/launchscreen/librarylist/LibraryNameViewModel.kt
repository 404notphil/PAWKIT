package com.tunepruner.fingerperc.launchscreen.librarylist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class LibraryNameViewModel(val app: Application, val soundpackID: String) : AndroidViewModel(app) {
    private val dataRepo = LibraryNameRepository(app, soundpackID)
    val libraryNameData = dataRepo.libraryListLiveData
    val soundpackData = dataRepo.soundpackListLiveData
}

class LibraryNameViewModelFactory(private val mApplication: Application, private val soundpackID: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LibraryNameViewModel(mApplication, soundpackID) as T
    }
}