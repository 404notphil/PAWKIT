package com.tunepruner.fingerperc.store.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tunepruner.fingerperc.databinding.LoadingInstrumentFragmentBinding

class LoadingInstrumentFragement :Fragment(){
    private lateinit var viewModel: LoadingInstrumentViewModel
    private lateinit var binding:LoadingInstrumentFragmentBinding

    override fun onCreateView(
        inflater:LayoutInflater,container:ViewGroup?,
        savedInstanceState:Bundle?
    ):View{
        binding=LoadingInstrumentFragmentBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(this).get(LoadingInstrumentViewModel::class.java)

//        with(binding.)

        return binding.root
    }

}

class LoadingInstrumentViewModel :ViewModel()