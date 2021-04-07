package com.tunepruner.fingerperc.launchscreen.librarydetail;

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.tunepruner.fingerperc.databinding.AppUpdateFragmentBinding;

class AppUpdateFragment : Fragment() {
    private lateinit var viewModel: AppUpdateViewModel
    private lateinit var binding: AppUpdateFragmentBinding

    override fun onCreateView(
        inflater:LayoutInflater, container: ViewGroup?,
        savedInstanceState:Bundle?
    ): View? {
        binding = AppUpdateFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(AppUpdateViewModel::class.java)

//        with(binding.)

        return binding.root
    }

}

class AppUpdateViewModel : ViewModel() {

}
