package com.tunepruner.fingerperc.launchscreen.librarylist

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.tunepruner.fingerperc.R

class LibraryListRecyclerFragment : Fragment(), LibraryListRecyclerAdapter.LibraryItemListener {
    private val TAG = "LibraryListRecyclerFragment.Class"
    private lateinit var viewModel: LibraryNameViewModel
    private lateinit var mListener: FragmentListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (context is FragmentListener) mListener = context as FragmentListener
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

    override fun onResume() {
        super.onResume()
        /*The code below is duplicated here in order to refresh the views that were
        modified with the progress bar on the last click*/
        viewModel.libraryNameData.observe(
            viewLifecycleOwner, {
                val adapter = LibraryListRecyclerAdapter(requireContext(), it, this)
                recyclerView.adapter = adapter
            })
    }

    interface FragmentListener {
        fun onFragmentFinished(libraryName: LibraryName)
    }

    override fun onLibraryItemClick(
        libraryName: LibraryName,
        progressBar: ProgressBar,
        recyclerButtonSubtitle: TextView
    ) {
//        libraryName.libraryName?.let { Log.i(TAG, it) }


        val interval = 10
        val amountOfIntervals = 10
        val duration = interval * amountOfIntervals
        when {
            libraryName.isReleased == false ||
                    libraryName.isReleased == null -> navController.navigate(R.id.comingSoonFragment)
            libraryName.isPurchased == false ||
                    libraryName.isPurchased == null -> navController.navigate(R.id.libraryDetailFragment)
            libraryName.isInstalled == false ||
                    libraryName.isInstalled == null -> navController.navigate(R.id.appUpdateFragment)
            else -> {
                showLoadingInstrument(
                    libraryName,
                    progressBar,
                    recyclerButtonSubtitle,
                    10,
                    100
                )
            }
        }

    }

    fun showLoadingInstrument(
        libraryName: LibraryName,
        progressBar: ProgressBar,
        recyclerButtonSubtitle: TextView,
        intervalLength: Int,
        amountOfIntervals: Int
    ) {
        recyclerButtonSubtitle.text = "Loading..."
        val handler = Handler(Looper.getMainLooper())

        var intervalsAccumulated = intervalLength
        for (i in 0..amountOfIntervals) {
            handler.postDelayed(
                {
                    progressBar.progress += amountOfIntervals/100
                }, intervalsAccumulated.toLong()
            )
            intervalsAccumulated += intervalLength
            Log.i(TAG, "${intervalsAccumulated}")
        }

        handler.postDelayed({
            recyclerButtonSubtitle.text = "Opening..."
            mListener.onFragmentFinished(libraryName)
        }, (intervalLength * amountOfIntervals).toLong())
    }

}
