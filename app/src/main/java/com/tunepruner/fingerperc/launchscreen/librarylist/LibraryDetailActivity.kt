package com.tunepruner.fingerperc.launchscreen.librarylist

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.tunepruner.fingerperc.R

class LibraryDetailActivity : YouTubeBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.library_detail_activity)

        val libraryID = intent.extras?.getString("libraryName") ?: ""
        findViewById<TextView>(R.id.titleOfLibraryDetail).text = libraryID

        val youtubePlayerView: YouTubePlayerView = findViewById(R.id.youtubePlayerView)
        playVideo("96LbtpRAMHc", youtubePlayerView)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun playVideo(videoId: String?, youTubePlayerView: YouTubePlayerView) {
        //initialize youtube player view
        youTubePlayerView.initialize("AIzaSyD99PbyAKuXxsKQuyDsb6PD-Xb7ffobd8c",
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider?,
                    youTubePlayer: YouTubePlayer, b: Boolean
                ) {
                    youTubePlayer.loadVideo(videoId)
                    Handler(Looper.getMainLooper()).post { youTubePlayer.pause() }
                    youTubePlayer.setPlayerStateChangeListener(object:YouTubePlayer.PlayerStateChangeListener{
                        override fun onLoading() {}
                        override fun onLoaded(p0: String?) {}
                        override fun onAdStarted() {}
                        override fun onVideoStarted() {}
                        override fun onVideoEnded() {
                            youTubePlayer.seekToMillis(0)
                            youTubePlayer.pause()
                        }
                        override fun onError(p0: YouTubePlayer.ErrorReason?) {}

                    })
                }

                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider?,
                    youTubeInitializationResult: YouTubeInitializationResult?
                ) {
                    Log.i("testing123", youTubeInitializationResult?.name.toString())
                }
            })
    }
}

