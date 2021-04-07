package com.tunepruner.fingerperc.launchscreen.librarylist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.tunepruner.fingerperc.R

class LibraryListRecyclerFragment : Fragment(), LibraryListRecyclerAdapter.LibraryItemListener{
    private val TAG = "LibraryListRecyclerFragment.Class"
    private lateinit var viewModel: LibraryNameViewModel
    private lateinit var mListener: FragmentListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var navController: NavController

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (context is FragmentListener) mListener = context as FragmentListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.launch_screen2, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.addItemDecoration(SpacesItemDecoration(50))
//        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        viewModel = ViewModelProvider(this).get(LibraryNameViewModel::class.java)
        viewModel.libraryNameData.observe(
            viewLifecycleOwner, {
                val adapter = LibraryListRecyclerAdapter(requireContext(), it, this)
                recyclerView.adapter = adapter
            })

        return view
    }
    //Todo review the first chapter of this course (https://www.linkedin.com/learning/android-development-essential-training-manage-data-with-kotlin/share-data-with-livedata-objects-2?contextUrn=urn%3Ali%3AlyndaLearningPath%3A5a724cba498e9ec2d506035e)

    interface FragmentListener{
        fun onFragmentFinished()
    }

    override fun onLibraryItemClick(libraryName: LibraryName) {
//        libraryName.libraryName?.let { Log.i(TAG, it) }
        navController.navigate(R.id.boomFragment)
    }
}