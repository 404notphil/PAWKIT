package com.tunepruner.fingerperc.launchscreen

import android.app.Activity
import android.net.sip.SipSession
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.tunepruner.fingerperc.R

class LaunchScreenActivity : AppCompatActivity(), LibraryListFragment.FragmentListener {

    private val TAG = "LaunchScreenActivity.Class"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        System.loadLibrary("bomboleguero")//TODO this is the JNI one, and shouldn't use the libraryName string. It should be refactored eventually!

        actionBar?.hide()
        setContentView(R.layout.main_activity)

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LibraryListFragment.newInstance())
                .commitNow()
        }
    }

    override fun onResume() {
        super.onResume()
        System.loadLibrary("bomboleguero")
        findViewById<ImageView>(R.id.cajon_button).visibility = View.VISIBLE
        findViewById<ImageView>(R.id.bombo_button).visibility = View.VISIBLE
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onFragmentFinished() {
        TODO("Not yet implemented")
    }

}
