package com.tunepruner.fingerperc.store.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.instrument.InstrumentActivity
import com.tunepruner.fingerperc.store.Library

class LaunchScreenActivity : AppCompatActivity(), LibraryListRecyclerFragment.FragmentListener {

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
