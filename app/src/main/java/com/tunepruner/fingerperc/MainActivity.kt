package com.tunepruner.fingerperc

import android.app.Activity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ActivityNavigator
import com.tunepruner.fingerperc.instrument.Instrument
import com.tunepruner.fingerperc.instrument.ScreenPrep


class MainActivity : AppCompatActivity() { //change signature to Instrument activity (val libraryName: String)
    lateinit var instrument: Instrument

    override fun onCreate(savedInstanceState: Bundle?) {
        val libraryName = intent.extras?.getString("libraryName")
        instrument = instrumentFactory(this, libraryName!!)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.playable_area_2)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        instrument.onTouch(event)
        guiFlicker(event)
        Log.i("coords", "event.y = ${event.y}")
        Log.i("coorgitds", "event.rawY = ${event.rawY}")
        Log.i("coords", "Screen dimensions = ${ScreenPrep.getDimensions(this)}")
        return true
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    fun guiFlicker(event: MotionEvent) {
        val dimensions = ScreenPrep.getDimensions(this)
        val maskedAction = event.actionMasked
        when (maskedAction) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.y > dimensions.screenHeight / 2) {
                    var imageView: ImageView = findViewById(R.id.headImage)
                    imageView.setImageResource(R.mipmap.head_no_blur_png_foreground)
                } else {
                    var imageView: ImageView = findViewById(R.id.rimImage)
                    imageView.setImageResource(R.mipmap.rim_no_blur_png_foreground)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                if (event.y > dimensions.screenHeight / 2) {
                    var imageView: ImageView = findViewById(R.id.headImage)
                    imageView.setImageResource(R.mipmap.head_png_foreground)
                } else {
                    var imageView: ImageView = findViewById(R.id.rimImage)
                    imageView.setImageResource(R.mipmap.rim_png_foreground)
                }
            }
        }
    }
}

fun instrumentFactory(activity: Activity, libraryName: String): Instrument {
    return Instrument(activity, libraryName)
}

