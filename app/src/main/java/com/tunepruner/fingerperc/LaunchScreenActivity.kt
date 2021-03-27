package com.tunepruner.fingerperc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.tunepruner.fingerperc.instrument.Instrument

class LaunchScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        System.loadLibrary("bomboleguero")//TODO this is the JNI one, and shouldn't use the libraryName string. It should be refactored eventually!

        actionBar?.hide()
        setContentView(R.layout.launch_screen)

        findViewById<ImageView>(R.id.cajon_button).setOnClickListener {
//
//            fadeOut(findViewById<ImageView>(R.id.cajon))
            val intent = Intent(this, InstrumentActivity::class.java).apply {
                putExtra("libraryName", "bomboleguero")
            }
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.bombo_button).setOnClickListener {
//            fadeOut(findViewById<ImageView>(R.id.bomboleguero))

            val intent = Intent(this, InstrumentActivity::class.java).apply {
                putExtra("libraryName", "cajon")
            }
            startActivity(intent)
        }
    }

    private fun fadeOut(viewToFadeOut: ImageView?) {
        val fadeOut: Animation = AlphaAnimation(1F, 0F)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.duration = 10

        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                viewToFadeOut!!.setVisibility(View.GONE)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationStart(animation: Animation?) {}
        })

        viewToFadeOut!!.startAnimation(fadeOut)
    }

    override fun onResume() {
        super.onResume()
        System.loadLibrary("bomboleguero")//TODO this is the JNI one, and shouldn't use the libraryName string, but should be refactored eventually!
    }
}