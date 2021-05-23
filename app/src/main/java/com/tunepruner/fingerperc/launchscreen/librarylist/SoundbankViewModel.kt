package com.tunepruner.fingerperc.launchscreen.librarylist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class SoundbankViewModel(val app: Application, val soundpackID: String) : AndroidViewModel(app) {
    private val dataRepo = SoundbankRepo(app, soundpackID)
    val soundbank = dataRepo.soundbank
    val connectionAchieved = false
}

class SoundbankViewModelFactory(
    private val mApplication: Application,
    private val soundpackID: String
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SoundbankViewModel(mApplication, soundpackID) as T
    }
}