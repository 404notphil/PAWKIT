package com.tunepruner.fingerperc.launchscreen.soundpackDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tunepruner.fingerperc.launchscreen.librarylist.LibraryNameRepository

class SoundpackViewModel(val app: Application) : AndroidViewModel(app) {
    private val dataRepo = SoundpackRepository(app)
    val libraryNameData = dataRepo.soundpackListLiveData
}