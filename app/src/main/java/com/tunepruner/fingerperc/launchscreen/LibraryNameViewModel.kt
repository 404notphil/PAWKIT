package com.tunepruner.fingerperc.launchscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class LibraryNameViewModel(app: Application): AndroidViewModel(app) {
    private val dataRepo = LibraryNameRepository(app)
    val libraryNameData = dataRepo.libraryNameData
}