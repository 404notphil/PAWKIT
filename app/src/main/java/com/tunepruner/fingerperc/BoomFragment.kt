package com.tunepruner.fingerperc

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tunepruner.fingerperc.databinding.BoomFragmentBinding

class BoomFragment : Fragment() {
    private lateinit var viewModel: BoomViewModel
    private lateinit var binding: BoomFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BoomFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(BoomViewModel::class.java)

//        with(binding.)

        return binding.root
    }

}