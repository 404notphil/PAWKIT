package com.tunepruner.fingerperc.launchscreen.librarydetail

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
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
import com.tunepruner.fingerperc.launchscreen.librarylist.LibraryListRecyclerFragmentDirections

class LibraryDetailFragment : Fragment(), BillingClientListener {
    private lateinit var skuDetails: SkuDetails
    private val logTag = "LbrryDetlFrgmnt.Class"
    private val args: LibraryDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentLibraryDetailBinding
    private var stopRequested = false
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

        binding.soundpackButton.text = "${args.soundpackID}"

        billingClientWrapper = BillingClientWrapper.getInstance(this, requireContext())

//        binding.soundpackButton.setOnClickListener{
//
//            val action =
//                LibraryDetailFragmentDirections.actionLibraryDetailFragment3ToSoundpackFragment(
//                    args.libraryname ?: "",
//                    args.libraryid ?: "",
//                    args.soundpackID ?: "",
//                    args.imageUrl ?: "",
//                    args.ispurchased ?: false
//                )
//            navController.navigate(action)
//
//        }

        //TODO change this to the play button
        binding.soundpackButton.setOnClickListener {
            if (binding.youtubePlayerView.alpha == 1F)
                binding.youtubePlayerView.alpha = 0F

            if (args.ispurchased) {
                val progressBarLength = 1000
                showLoadingInstrument(progressBarLength / 100, 100)
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    if (!stopRequested) {
                        val intent =
                            Intent(requireActivity(), InstrumentActivity::class.java).apply {
                                putExtra("libraryID", args.libraryid)
                            }
                        startActivity(intent)
                    }
                }, progressBarLength.toLong())
            } else {
                billingClientWrapper.querySkuDetails(requireActivity(), args.soundpackID)
            }
        }

        binding.howToPlayButton.setOnClickListener {
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
        Glide.with(requireContext())
            .load(args.imageUrl)
            .into(binding.libraryDetailImage)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRequested = true
    }

    private fun showLoadingInstrument(intervalLength: Int, amountOfIntervals: Int) {
        binding.loadingStatusText.text = "Loading..."
        binding.progressBar2.alpha = 1F
        binding.loadingStatusText.alpha = 1F

        val handler = Handler(Looper.getMainLooper())

        var intervalsAccumulated = intervalLength
        for (i in 0..amountOfIntervals) {
            handler.postDelayed(
                {
                    if (!stopRequested) {
                        binding.progressBar2.progress += amountOfIntervals / 100
                        Log.i(logTag, "progressing")
                    }
                }, intervalsAccumulated.toLong()
            )
            intervalsAccumulated += intervalLength
        }

        handler.postDelayed({
            if (!stopRequested) {
                binding.loadingStatusText.text = "Opening..."
            }
        }, (intervalLength * amountOfIntervals).toLong() - 200)

        handler.postDelayed({
            if (!stopRequested) {
                binding.progressBar2.alpha = 0F
                binding.progressBar2.progress = 0
                binding.loadingStatusText.alpha = 0F
            }
        }, intervalsAccumulated.toLong())
    }

    override fun onClientReady() {
//        billingClientWrapper.querySkuDetails(requireActivity(), args.soundpackID)
    }

    override fun onPurchasesQueried(soundpacksPurchased: ArrayList<Purchase>) {
        //do nothing
    }

}
