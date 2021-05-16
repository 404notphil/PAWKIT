package com.tunepruner.fingerperc.launchscreen.librarylist

import android.app.Application
import androidx.lifecycle.AndroidViewModel


class LibraryNameViewModel(val app: Application) : AndroidViewModel(app) {
    private val dataRepo = LibraryNameRepository(app)
    val libraryNameData = dataRepo.libraryListLiveData
    val soundpackData = dataRepo.soundpackListLiveData
}

