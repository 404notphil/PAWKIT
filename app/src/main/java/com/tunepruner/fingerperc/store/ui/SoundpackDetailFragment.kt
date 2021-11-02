package com.tunepruner.fingerperc.store.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.databinding.FragmentSoundpackDetailBinding
import com.tunepruner.fingerperc.store.Library
import com.tunepruner.fingerperc.store.Soundbank
import com.tunepruner.fingerperc.launchscreen.librarylist.*
import com.tunepruner.fingerperc.store.viewmodel.SoundbankViewModel
import com.tunepruner.fingerperc.store.viewmodel.SoundbankViewModelFactory
import com.tunepruner.fingerperc.store.BillingClientListener
import com.tunepruner.fingerperc.store.BillingClientWrapper

class SoundpackDetailFragment : Fragment(), LibraryListRecyclerAdapter.LibraryItemListener,
    BillingClientListener {
    private val args: LibraryDetailFragmentArgs by navArgs()
    private val TAG = "SnpkDetFgmt.Class"
    private val viewModel: SoundbankViewModel by viewModels {
        SoundbankViewModelFactory(
            requireActivity().application,
            args.soundpackID
        )
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSoundpackDetailBinding
    private lateinit var purchasesUpdatedListener: PurchasesUpdatedListener
    lateinit var billingClientWrapper: BillingClientWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getData()
        Log.i(TAG, "onCreate: ")
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSoundpackDetailBinding.inflate(inflater, container, false)
        recyclerView = binding.root.findViewById(R.id.recyclerView)

        recyclerView.addItemDecoration(SpacesItemDecoration(50))

        viewModel.soundbankLiveData.observe(viewLifecycleOwner) { soundbank ->
            val adapter = SoundpackRecyclerAdapter(
                requireContext(),
                soundbank,
                this,
                args.soundpackID
            )
            recyclerView.adapter = adapter
        }


        binding.soundpackTitle.text = args.soundpackname

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.ispurchased) {
            requireActivity().findViewById<LinearLayout>(R.id.buttons_parent)
                .removeView(requireActivity().findViewById<Button>(R.id.button2))
        } else {
            binding.button2.text = args.price
        }
    }

    override fun onResume() {
        super.onResume()

        billingClientWrapper = BillingClientWrapper.getInstance(this, requireContext())
        binding.button2.text = args.price
    }

    override fun onLibraryItemClick(
        library: Library,
        recyclerButtonSubtitle: TextView
    ) {
        when {
            viewModel.soundbankLiveData.value?.check(Soundbank.CheckType.IS_RELEASED, library) == false ||
                    viewModel.soundbankLiveData.value?.check(
                        Soundbank.CheckType.IS_RELEASED,
                        library
                    ) == null -> {
//                navController.navigate(R.id.comingSoonFragment)
                val text = "Coming soon!"
                val toastDuration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(context, text, toastDuration)
                toast.show()
            }
            viewModel.soundbankLiveData.value?.check(
                Soundbank.CheckType.IS_INSTALLED,
                library
            ) == false && viewModel.soundbankLiveData.value?.check(
                Soundbank.CheckType.IS_PURCHASED,
                library
            ) == true -> {
                val intent = Intent(requireActivity(), UpdateDialogActivity::class.java)
                startActivity(intent)
            }
            else -> {
                val action =
                    SoundpackDetailFragmentDirections.actionSoundpackFragmentToLibraryDetailFragment3(
                        library.libraryName ?: "",
                        library.libraryID ?: "",
                        library.soundpackID ?: "",
                        library.imageUrl ?: "",
                        viewModel.soundbankLiveData.value?.check(Soundbank.CheckType.IS_PURCHASED, library)
                            ?: false,
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

    override fun onClientReady() {
//        billingClientWrapper.querySkuDetails(requireActivity(), args.soundpackID)
    }

    override fun onPurchasesQueried(soundpacksPurchased: ArrayList<Purchase>) {
        //do nothing
    }
}