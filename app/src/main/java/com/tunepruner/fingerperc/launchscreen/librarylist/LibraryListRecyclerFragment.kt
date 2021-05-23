package com.tunepruner.fingerperc.launchscreen.librarylist

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.launchscreen.librarydetail.Library
import com.tunepruner.fingerperc.launchscreen.librarydetail.Soundbank
import java.net.InetAddress


class LibraryListRecyclerFragment : Fragment(), LibraryListRecyclerAdapter.LibraryItemListener {
    private val TAG = "LibraryListRecyclerFragment.Class"
    private val wasSet = false
    private val viewModel: SoundbankViewModel by viewModels {
        SoundbankViewModelFactory(
            requireActivity().application,
            ""
        )
    }
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
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        observeLiveData()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//            Log.i(TAG, "internetAvail: ${isInternetAvailable()}")
//        if (!isInternetAvailable()) {
//            val parent: LinearLayout = requireActivity().findViewById(R.id.recycler_parent)
//            parent.removeAllViews()
//
//            val textView = TextView(requireContext())
//            with(textView) {
//                text = "An internet connection is needed to run PAWKIT for the first time. Please connect to the internet, and restart the app!"
//                setTextColor(Color.WHITE)
//                textSize = 20F
//                gravity = Gravity.CENTER
//                textAlignment = View.TEXT_ALIGNMENT_CENTER
//                parent.addView(this)
//            }
//
//        }
    }


    //Todo review the first chapter of this course (https://www.linkedin.com/learning/android-development-essential-training-manage-data-with-kotlin/share-data-with-livedata-objects-2?contextUrn=urn%3Ali%3AlyndaLearningPath%3A5a724cba498e9ec2d506035e)

    override fun onResume() {
        super.onResume()
        observeLiveData()
    }

//    fun isInternetAvailable(): Boolean {
//        return try {
//            val ipAddr: InetAddress = InetAddress.getByName("https://google.com")
//            //You can replace it with your name
//            !ipAddr.equals("")
//        } catch (e: Exception) {
//            false
//        }
//    }

    private fun observeLiveData() {
        viewModel.soundbank.observe(viewLifecycleOwner) { soundbank ->
            val adapter = LibraryListRecyclerAdapter(
                requireContext(),
                soundbank,
                this
            )
            recyclerView.adapter = adapter

        }
    }


    override fun onLibraryItemClick(
        library: Library,
        recyclerButtonSubtitle: TextView
    ) {
        val interval = 10
        val amountOfIntervals = 10
        val duration = interval * amountOfIntervals
        when {
            viewModel.soundbank.value?.check(Soundbank.CheckType.IS_RELEASED, library) == false ||
                    viewModel.soundbank.value?.check(Soundbank.CheckType.IS_RELEASED, library) == null -> {
//                navController.navigate(R.id.comingSoonFragment)
                val text = "Coming soon!"
                val toastDuration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(context, text, toastDuration)
                toast.show() }
            viewModel.soundbank.value?.check(Soundbank.CheckType.IS_INSTALLED, library) == false &&
                    viewModel.soundbank.value?.check(Soundbank.CheckType.IS_PURCHASED, library) == true -> {
                val intent = Intent(requireActivity(), UpdateDialogActivity::class.java)
                startActivity(intent)
            }
            else -> {
                val action =
                    LibraryListRecyclerFragmentDirections.actionLaunchScreenFragmentToLibraryDetailFragment3(
                        library.libraryName ?: "",
                        library.libraryID ?: "",
                        library.soundpackID ?: "",
                        library.imageUrl ?: "",
                        viewModel.soundbank.value?.check(Soundbank.CheckType.IS_PURCHASED, library)?: false,
                        "$0.99",
                        library.soundpackName ?: "(unknown name)"
                    )
                val recyclerButtonImage =
                    requireActivity().findViewById<ImageView>(R.id.recycler_button_image)
                val extras =
                    FragmentNavigatorExtras(recyclerButtonImage to "${library.soundpackID}")
                navController.navigate(action, extras)
            }
        }
    }

    interface FragmentListener {
        fun onFragmentFinished(library: Library)
    }
}
