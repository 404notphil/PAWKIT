package com.tunepruner.fingerperc.launchscreen.librarydetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel

import com.tunepruner.fingerperc.databinding.LoadingInstrumentFragmentBinding

class LoadingInstrumentFragement :Fragment(){
    private lateinit var viewModel:LoadingInstrumentViewModel
    private lateinit var binding:LoadingInstrumentFragmentBinding

    override fun onCreateView(
        inflater:LayoutInflater,container:ViewGroup?,
        savedInstanceState:Bundle?
    ):View?{
        binding=LoadingInstrumentFragmentBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(this).get(LoadingInstrumentViewModel::class.java)

//        with(binding.)

        return binding.root
    }

}

class LoadingInstrumentViewModel :ViewModel()