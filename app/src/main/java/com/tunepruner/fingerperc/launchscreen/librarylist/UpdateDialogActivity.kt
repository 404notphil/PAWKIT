package com.tunepruner.fingerperc.launchscreen.librarylist

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.tunepruner.fingerperc.R

class UpdateDialogActivity : Activity() {

    private val TAG = "UpdateDialogActivity.Class"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setTheme(R.style.Theme_MyTheme); // can either use R.style.AppTheme_Dialog or R.style.AppTheme as deined in styles.xml
        setContentView(R.layout.app_update_activity)
    }

    override fun onResume() {
        super.onResume()
        findViewById<Button>(R.id.update_button).setOnClickListener {
            Log.i(TAG, "Button pressed")
        }
        findViewById<Button>(R.id.postpone_update_button).setOnClickListener {
            Log.i(TAG, "Other button pressed")

        }
    }
}