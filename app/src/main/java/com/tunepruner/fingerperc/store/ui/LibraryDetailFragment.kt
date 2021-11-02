package com.tunepruner.fingerperc.store.ui

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.android.billingclient.api.*
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.databinding.FragmentLibraryDetailBinding
import com.tunepruner.fingerperc.instrument.InstrumentActivity
import com.tunepruner.fingerperc.store.BillingClientListener
import com.tunepruner.fingerperc.store.BillingClientWrapper

class LibraryDetailFragment : Fragment(), BillingClientListener {
    private var player: YouTubePlayer? = null
    private val args: LibraryDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentLibraryDetailBinding
    private var stopLoadingRequested = false
    private lateinit var billingClientWrapper: BillingClientWrapper
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

    override fun onResume() {
        super.onResume()

        binding.youtubePlayerView.alpha = 0F

        binding.titleOfLibraryDetail.text = args.libraryname

        binding.soundpackButton.text =
            context?.getString(R.string.view_soundpack, args.soundpackname)

        billingClientWrapper = BillingClientWrapper.getInstance(this, requireContext())

        binding.soundpackButton

        binding.libraryDetailImage.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(null)
        }

        binding.soundpackButton.setOnClickListener {
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
            with(binding.youtubePlayerView) {
                if (this.alpha == 1f) this.alpha = 0F
            }

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
                binding.youtubePlayerView.apply {
                    alpha = 0f
                    visibility = View.VISIBLE
                    animate()
                        .alpha(1f)
                        .setDuration(500)
                        .setListener(null)
                }
            else {
                player?.pause()
                binding.youtubePlayerView.apply {
                    val view = this
                    alpha = 1f
                    visibility = View.VISIBLE
                    animate()
                        .alpha(0f)
                        .setDuration(500)
                        .setListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator?) {}
                            override fun onAnimationCancel(animation: Animator?) {}
                            override fun onAnimationRepeat(animation: Animator?) {}
                            override fun onAnimationEnd(animation: Animator?) {
                                view.visibility = View.GONE
                            }
                        })

                }
            }
        }

        val youTubePlayerView: YouTubePlayerView = binding.youtubePlayerView
        youTubePlayerView.enterFullScreen()

        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(
            object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    player = youTubePlayer.apply { cueVideo("48E8RgHFo30", 0F) }
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

        loadingText.text = getString(R.string.loading)

        val handler = Handler(Looper.getMainLooper())

        var intervalsAccumulated = intervalLength
        loop@ for (i in 0..amountOfIntervals) {
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
                loadingText.text = getString(R.string.opening)
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
        buttonParent.removeView(titleOfLibrary)
        buttonParent.removeView(progressBarLayout)
        buttonParent.addView(progressBarLayout, 0)
    }

    private fun onProgressFinished(
        buttonParent: ViewGroup,
        titleOfLibrary: TextView,
        progressBarLayout: LinearLayout
    ) {
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
