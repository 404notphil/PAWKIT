package com.tunepruner.fingerperc.launchscreen.librarydetail

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.android.billingclient.api.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.tunepruner.fingerperc.InstrumentActivity
import com.tunepruner.fingerperc.databinding.FragmentLibraryDetailBinding
import com.tunepruner.fingerperc.launchscreen.librarylist.BillingClientWrapper

class LibraryDetailFragment : Fragment() {
    private lateinit var skuDetails: SkuDetails
    private val logTag = "LibraryDetailFragment.Class"
    private val args: LibraryDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentLibraryDetailBinding
    private var stopRequested = false
    private lateinit var purchasesUpdatedListener: PurchasesUpdatedListener
    lateinit var billingClientWrapper: BillingClientWrapper
    private val TAG = "LibraryDetailFragment.class"

    override fun onResume() {
        super.onResume()
        binding.titleOfLibraryDetail.text = args.libraryname

        binding.button.text = if (args.ispurchased) "Play now!" else "$0.99"

        billingClientWrapper = BillingClientWrapper.getInstance(requireActivity(), args.soundpackID)

        binding.button.setOnClickListener {
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
                billingClientWrapper.flow(requireActivity())
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
                    youTubePlayer.cueVideo("UlaJpGEDFVM", 0F)
                    youTubePlayerView.alpha = 1F
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

}
