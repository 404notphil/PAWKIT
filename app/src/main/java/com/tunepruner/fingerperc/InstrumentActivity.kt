package com.tunepruner.fingerperc

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.tunepruner.fingerperc.gui.InstrumentGUI
import com.tunepruner.fingerperc.instrument.Instrument
import com.tunepruner.fingerperc.instrument.ScreenPrep


class InstrumentActivity :
    AppCompatActivity() { //change signature to Instrument activity (val libraryName: String)
    lateinit var instrument: Instrument
    lateinit var libraryName: String
    private val TAG = "InstrumentActivity"
    private lateinit var usageReportingService: UsageReportingService

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        libraryName = intent.extras?.getString("libraryName")!!
        instrument = instrumentFactory(this, libraryName)
//        Log.i("MainActivity", libraryName)
        super.onCreate(savedInstanceState)

        usageReportingService = UsageReportingService(this)
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        usageReportingService.startClock()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        instrument.onTouch(event)
//        guiFlicker(event)
//        Log.i(TAG, "event.y = ${event.y}")
//        Log.i(TAG, "event.rawY = ${event.rawY}")
//        Log.i(TAG, "Screen dimensions = ${ScreenPrep.getDimensions(this)}")
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


    override fun onPause() {
        super.onPause()
        instrument.tearDownPlayer()
        usageReportingService.stopClock()
    }

    override fun onDestroy() {
        super.onDestroy()
        instrument.tearDownPlayer()
    }


}

fun instrumentFactory(activity: Activity, libraryName: String): Instrument {
    return Instrument(activity, libraryName, InstrumentGUI(activity, libraryName))
}

