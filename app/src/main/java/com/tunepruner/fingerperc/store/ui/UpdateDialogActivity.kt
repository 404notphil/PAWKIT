package com.tunepruner.fingerperc.store.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.UpdateAvailability
import com.tunepruner.fingerperc.R

class UpdateDialogActivity : Activity() {
    lateinit var appUpdateManager: AppUpdateManager
    private val TAG = "UpdateDialogActivity.Class"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setTheme(R.style.Theme_MyTheme) // can either use R.style.AppTheme_Dialog or R.style.AppTheme as deined in styles.xml
        setContentView(R.layout.app_update_activity)
        appUpdateManager = AppUpdateManagerFactory.create(this)
    }

    override fun onResume() {
        super.onResume()
        findViewById<Button>(R.id.update_button).setOnClickListener {
            val appUpdateInfoTask = appUpdateManager.appUpdateInfo

            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                Log.i(TAG, "appUpdateInfo: ${appUpdateInfo.installStatus()}")
                if (
                    appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    || appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                    && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)
                ) {

                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo, IMMEDIATE, this, 2374987
                    )

                } else {
                    Toast.makeText(this, "No update available!", Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "No update available!")
                }
            }

            appUpdateInfoTask.addOnFailureListener { appUpdateInfo ->
                Log.i(TAG, "Failure")
            }
        }

        findViewById<Button>(R.id.postpone_update_button).setOnClickListener {
            val intent = Intent(this, LaunchScreenActivity::class.java)
            startActivity(intent)
        }
    }
}