package com.tunepruner.fingerperc.launchscreen.soundpackDetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.launchscreen.librarydetail.LibraryDetailFragmentArgs
import com.tunepruner.fingerperc.launchscreen.librarylist.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SoundpackDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SoundpackDetailFragment : Fragment(), LibraryListRecyclerAdapter.LibraryItemListener {
    private val args: LibraryDetailFragmentArgs by navArgs()
    private val TAG = "SnpkDetFgmt.Class"
    private val viewModel: SoundpackViewModel by viewModels { MyViewModelFactory(requireActivity().application, args.soundpackID) }
    private lateinit var recyclerView: RecyclerView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewModel.soundpackData.observe(this) {
            val adapter = SoundpackRecyclerAdapter(requireContext(), it, this)
            recyclerView.adapter = adapter
        }
        Log.i(TAG, "onCreate: ")
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_soundpack_detail, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.addItemDecoration(SpacesItemDecoration(50))
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SoundpackFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SoundpackDetailFragment()
                .apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onLibraryItemClick(
        libraryDetails: LibraryDetails,
        recyclerButtonSubtitle: TextView
    ) {
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
                val intent = Intent(requireActivity(), UpdateDialogActivity::class.java)
                startActivity(intent)
            }
            else -> {
                val action =
                    LibraryListRecyclerFragmentDirections.actionLaunchScreenFragmentToLibraryDetailFragment3(
                        libraryDetails.libraryName ?: "",
                        libraryDetails.libraryID ?: "",
                        libraryDetails.soundpackID ?: "",
                        libraryDetails.imageUrl ?: "",
                        libraryDetails.isPurchased ?: false
                    )
                val recyclerButtonImage =
                    requireActivity().findViewById<ImageView>(R.id.recycler_button_image)
                val extras =
                    FragmentNavigatorExtras(recyclerButtonImage to "${libraryDetails.soundpackID}")
                navController.navigate(action, extras)
            }
        }
    }
}