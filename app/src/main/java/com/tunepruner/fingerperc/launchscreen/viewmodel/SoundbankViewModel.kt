package com.tunepruner.fingerperc.launchscreen.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tunepruner.fingerperc.launchscreen.data.SoundbankRepo

class SoundbankViewModel(val app: Application, val soundpackID: String) : AndroidViewModel(app) {
    private val dataRepo = SoundbankRepo(app, soundpackID)
    val soundbankLiveData = dataRepo.soundbankLiveData
    val connectionAchieved = false

    fun getData(){
        dataRepo.populateFromFirestore("libraries")
    }
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