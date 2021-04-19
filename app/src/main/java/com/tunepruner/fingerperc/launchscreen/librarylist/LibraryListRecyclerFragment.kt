package com.tunepruner.fingerperc.launchscreen.librarylist

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
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
        fun onFragmentFinished(libraryDetails: LibraryDetails)
    }

    override fun onLibraryItemClick(
        libraryDetails: LibraryDetails,
        progressBar: ProgressBar,
        recyclerButtonSubtitle: TextView
    ) {

//        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context);
        val interval = 10
        val amountOfIntervals = 10
        val duration = interval * amountOfIntervals
        when {
            libraryDetails.isReleased == false ||
                    libraryDetails.isReleased == null -> {
//                navController.navigate(R.id.comingSoonFragment)
                val text = "Coming soon!"
                val toastDuration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(context, text, toastDuration)
                toast.show()
            }
            libraryDetails.isInstalled == false &&
                    libraryDetails.isPurchased == true -> {

//                    navController.navigate(R.id.appUpdateFragment)
//                    alertDialogBuilder.create()
//                UpdatePrompt().show(parentFragmentManager, "")
                val intent = Intent(requireActivity(), UpdateDialogActivity::class.java)
                startActivity(intent)
            }
            else -> {
                val action =
                    LibraryListRecyclerFragmentDirections.actionLaunchScreenFragmentToLibraryDetailFragment3(
                        libraryDetails.libraryName ?: "",
                        libraryDetails.libraryID ?: "",
                        libraryDetails.soundpackID ?: "",
                        libraryDetails.isPurchased ?: true
                    )
                navController.navigate(action)
            }
        }

    }


}

class UpdatePrompt : DialogFragment() {
    private val TAG: String = "UpdatePrompt.Class"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = it.layoutInflater
            builder.setView(inflater.inflate(R.layout.app_update_activity, null))
            return builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}