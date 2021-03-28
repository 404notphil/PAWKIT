package com.tunepruner.fingerperc

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tunepruner.fingerperc.instrument.Instrument
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LaunchScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        System.loadLibrary("bomboleguero")//TODO this is the JNI one, and shouldn't use the libraryName string. It should be refactored eventually!

        actionBar?.hide()
        setContentView(R.layout.launch_screen)

        findViewById<ImageView>(R.id.cajon_button).visibility = View.VISIBLE
        findViewById<ImageView>(R.id.bombo_button).visibility = View.VISIBLE

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        findViewById<ImageView>(R.id.cajon_button).setOnClickListener {
            fadeOut(findViewById(R.id.cajon_button))
            val intent = Intent(this, InstrumentActivity::class.java).apply {
                putExtra("libraryName", "cajon")
            }
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.bombo_button).setOnClickListener {
            fadeOut(findViewById(R.id.bombo_button))
            val intent = Intent(this, InstrumentActivity::class.java).apply {
                putExtra("libraryName", "bomboleguero")
            }
            startActivity(intent)
        }
    }

    private fun fadeOut(viewToFadeOut: View) {
        val fadeOut: Animation = AlphaAnimation(1F, 0F)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.duration = 10

        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                viewToFadeOut.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationStart(animation: Animation?) {}
        })

        viewToFadeOut.startAnimation(fadeOut)
    }
    private fun fadeIn(viewToFadeOut: View) {
        val fadeOut: Animation = AlphaAnimation(0F, 1F)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.duration = 500

        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                viewToFadeOut.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationStart(animation: Animation?) {}
        })

        viewToFadeOut.startAnimation(fadeOut)
    }

    override fun onResume() {
        super.onResume()
        findViewById<ImageView>(R.id.cajon_button).visibility = View.VISIBLE
        findViewById<ImageView>(R.id.bombo_button).visibility = View.VISIBLE
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        System.loadLibrary("bomboleguero")//TODO this is the JNI one, and shouldn't use the libraryName string, but should be refactored eventually!
    }

    fun animateButtonPress(imageID: Int) {
//        findViewById<ImageView>(imageID).scaleX = 2.0F
//        findViewById<ImageView>(imageID).scaleY = 2.0F

    }
}