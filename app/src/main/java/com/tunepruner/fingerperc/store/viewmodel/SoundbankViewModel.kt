package com.tunepruner.fingerperc.store.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tunepruner.fingerperc.store.repos.SoundbankRepo
import com.tunepruner.fingerperc.store.Soundbank

class SoundbankViewModel(val app: Application, val soundpackID: String) : AndroidViewModel(app) {
    private val dataRepo = SoundbankRepo(app, soundpackID)
    val soundbankLiveData: LiveData<Soundbank>
        get() {
            return dataRepo.soundbankLiveData
        }
    val connectionAchieved = false

    fun getData() {
        dataRepo.getCollectionFromFirestore()
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