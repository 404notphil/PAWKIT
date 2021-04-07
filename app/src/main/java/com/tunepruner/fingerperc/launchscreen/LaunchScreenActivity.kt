package com.tunepruner.fingerperc.launchscreen

import android.content.Intent
import android.os.Bundle
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.tunepruner.fingerperc.InstrumentActivity
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.launchscreen.librarylist.LibraryListRecyclerFragment
import com.tunepruner.fingerperc.launchscreen.librarylist.LibraryName

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
//        findViewById<ImageView>(R.id.recycler_button_image).visibility = View.VISIBLE
//        findViewById<ImageView>(R.id.bombo_button).visibility = View.VISIBLE
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onFragmentFinished(libraryName: LibraryName) {
        val intent = Intent(this, InstrumentActivity::class.java).apply {
            putExtra("libraryID", "${libraryName.libraryID}")
        }
        startActivity(intent)


    }

}
