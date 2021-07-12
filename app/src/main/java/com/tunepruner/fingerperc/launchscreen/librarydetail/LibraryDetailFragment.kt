package com.tunepruner.fingerperc.launchscreen.librarydetail

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.android.billingclient.api.*
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.tunepruner.fingerperc.InstrumentActivity
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.databinding.FragmentLibraryDetailBinding
import com.tunepruner.fingerperc.launchscreen.librarylist.BillingClientListener
import com.tunepruner.fingerperc.launchscreen.librarylist.BillingClientWrapper

class LibraryDetailFragment : Fragment(), BillingClientListener {
    private lateinit var skuDetails: SkuDetails
    private val logTag = "LbrryDetlFrgmnt.Class"
    private val args: LibraryDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentLibraryDetailBinding
    private var stopLoadingRequested = false
    private lateinit var purchasesUpdatedListener: PurchasesUpdatedListener
    lateinit var billingClientWrapper: BillingClientWrapper
    private val TAG = "LibraryDetailFragment.class"
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fade)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    override fun onResume() {
        super.onResume()

        binding.youtubePlayerView.alpha = 0F

        binding.titleOfLibraryDetail.text = args.libraryname

        binding.soundpackButton.text = "View soundpack: \"${args.soundpackname}\""

        billingClientWrapper = BillingClientWrapper.getInstance(this, requireContext())

        binding.soundpackButton

        binding.soundpackButton.setOnClickListener {
            Log.i(TAG, "clicked: ")
            stopLoadingRequested = true
            val action =
                LibraryDetailFragmentDirections.actionLibraryDetailFragment3ToSoundpackFragment(
                    args.libraryname,
                    args.libraryid,
                    args.soundpackID,
                    args.imageUrl,
                    args.ispurchased,
                    "$0.99",
                    args.soundpackname
                )
            navController.navigate(action)
        }

        binding.mainMenu.setOnClickListener {
            stopLoadingRequested = true
            val action =
                LibraryDetailFragmentDirections.actionLibraryDetailFragment3ToLoadingInstrumentFragment3()/*This action is incorrectly named but can't be edited*/
            navController.navigate(action)
        }

        binding.playButton.setOnClickListener {
            stopLoadingRequested = false
            if (binding.youtubePlayerView.alpha == 1F)
                binding.youtubePlayerView.alpha = 0F

            if (args.ispurchased) {
                val progressBarDuration = 1000

                showLoadingInstrument(progressBarDuration)

                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    if (!stopLoadingRequested) {
                        val intent =
                            Intent(requireActivity(), InstrumentActivity::class.java).apply {
                                putExtra("libraryID", args.libraryid)
                            }
                        startActivity(intent)
                    }
                }, progressBarDuration.toLong())
            } else {
                billingClientWrapper.querySkuDetails(requireActivity(), args.soundpackID)
            }
        }

        binding.howToPlayButton.setOnClickListener {
            stopLoadingRequested = true
            if (binding.youtubePlayerView.alpha == 0F)
                binding.youtubePlayerView.alpha = 1F
            else {
                binding.youtubePlayerView.alpha = 0F
            }
        }

        val youTubePlayerView: YouTubePlayerView = binding.youtubePlayerView
        youTubePlayerView.enterFullScreen()

        Log.i(logTag, "Resuming")

        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(
            object :
                AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo("48E8RgHFo30", 0F)
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonParent = requireActivity().findViewById<LinearLayout>(R.id.play_now_layout)
        val buttonToRemove = requireActivity().findViewById<LinearLayout>(R.id.play_button_parent)


        if (!args.ispurchased) { //ToDo remove djembe condition once samples are done!
            buttonParent.removeView(buttonToRemove)
        }

        Glide.with(requireContext())
            .load(args.imageUrl)
            .into(binding.libraryDetailImage)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLoadingRequested = true
    }

    private fun showLoadingInstrument(progressBarDuration: Int) {
        Log.i(TAG, "showLoadingInstrument: ")
        val intervalLength = 10
        val amountOfIntervals = progressBarDuration / intervalLength

        val parent = requireActivity().findViewById<LinearLayout>(R.id.library_detail_main)
        val titleOfLibrary = requireActivity().findViewById<TextView>(R.id.title_of_library_detail)
        layoutInflater.inflate(R.layout.progress_bar_layout, parent, true)
        val progressBarLayout =
            requireActivity().findViewById<LinearLayout>(R.id.progress_bar_layout)

        val progressBar = requireActivity().findViewById<ProgressBar>(R.id.progressBar2)
        val loadingText = requireActivity().findViewById<TextView>(R.id.loading_status_text)

        onProgressStarted(parent, titleOfLibrary, progressBarLayout)

        loadingText.text = "Loading..."

        val handler = Handler(Looper.getMainLooper())

        var intervalsAccumulated = intervalLength
        loop@for (i in 0..amountOfIntervals) {
            handler.postDelayed(
                {
                    if (!stopLoadingRequested) {
                        progressBar.progress += amountOfIntervals / 100
                    } else {
                        onProgressFinished(parent, titleOfLibrary, progressBarLayout)
                    }
                }, intervalsAccumulated.toLong()
            )
            intervalsAccumulated += intervalLength
        }

        handler.postDelayed({
            if (!stopLoadingRequested) {
                loadingText.text = "Opening..."
            }
        }, (intervalLength * amountOfIntervals).toLong() - 200)

        handler.postDelayed({
            if (!stopLoadingRequested) {
                onProgressFinished(parent, titleOfLibrary, progressBarLayout)
            }
        }, intervalsAccumulated.toLong())

    }

    private fun onProgressStarted(
        buttonParent: ViewGroup,
        titleOfLibrary: TextView,
        progressBarLayout: LinearLayout
    ) {
        Log.i(TAG, "onProgressStarted: ")
        buttonParent.removeView(titleOfLibrary)
        buttonParent.removeView(progressBarLayout)
        buttonParent.addView(progressBarLayout, 0)
    }

    private fun onProgressFinished(
        buttonParent: ViewGroup,
        titleOfLibrary: TextView,
        progressBarLayout: LinearLayout
    ) {
        Log.i(TAG, "onProgressFinished: ")
        buttonParent.removeView(progressBarLayout)
        buttonParent.removeView(titleOfLibrary)
        buttonParent.addView(titleOfLibrary, 0)
        binding.titleOfLibraryDetail.text = "..."
    }

    override fun onClientReady() {
//        billingClientWrapper.querySkuDetails(requireActivity(), args.soundpackID)
    }

    override fun onPurchasesQueried(soundpacksPurchased: ArrayList<Purchase>) {
        //do nothing
    }
}
