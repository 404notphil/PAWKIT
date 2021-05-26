package com.tunepruner.fingerperc.launchscreen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tunepruner.fingerperc.InstrumentActivity
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.launchscreen.librarydetail.Library
import com.tunepruner.fingerperc.launchscreen.librarylist.LibraryListRecyclerFragment
import java.io.File
import java.util.*

class LaunchScreenActivity : AppCompatActivity(), LibraryListRecyclerFragment.FragmentListener {
    private val TAG = "LaunchScreenActivity.Class"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        System.loadLibrary("bomboleguero")//TODO this is the JNI one, and shouldn't use the libraryName string. It should be refactored eventually!

        actionBar?.hide()
        setContentView(R.layout.main_activity_testing_navhost)

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onResume() {
        super.onResume()
        System.loadLibrary("bomboleguero")
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }


    override fun onFragmentFinished(library: Library) {
        val intent = Intent(this, InstrumentActivity::class.java).apply {
            putExtra("libraryID", "${library.libraryID}")
        }
        startActivity(intent)
    }


}
