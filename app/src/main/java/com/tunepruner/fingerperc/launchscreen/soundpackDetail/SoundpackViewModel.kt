package com.tunepruner.fingerperc.launchscreen.soundpackDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SoundpackViewModel(val app: Application, filter: String) : AndroidViewModel(app) {
    private val dataRepo = SoundpackRepository(app, filter)
    val soundpackData = dataRepo.libraryListLiveData
}

class MyViewModelFactory(private val mApplication: Application, private val mParam: String) :
    ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SoundpackViewModel(mApplication, mParam) as T
    }

}