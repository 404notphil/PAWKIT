package com.tunepruner.fingerperc

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tunepruner.fingerperc.gui.InstrumentGUI
import com.tunepruner.fingerperc.instrument.Instrument
import com.tunepruner.fingerperc.launchscreen.data.UsageReportingService


class InstrumentActivity : AppCompatActivity() {
    lateinit var instrument: Instrument
    lateinit var libraryID: String
    private val TAG = "InstrumentActivity"
    private lateinit var usageReportingService: UsageReportingService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        libraryID = intent.extras?.getString("libraryID") ?: ""

        usageReportingService = UsageReportingService(this)
        instrument = instrumentFactory(this, libraryID)

    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        instrument = instrumentFactory(this, libraryID)
        usageReportingService.startClock()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        instrument.onTouch(event, this)
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
        usageReportingService.stopClock()
    }
}

fun instrumentFactory(activity: Activity, libraryName: String): Instrument {
    return Instrument(activity, libraryName, InstrumentGUI(libraryName))
}

