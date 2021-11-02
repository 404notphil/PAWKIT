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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setTheme(R.style.Theme_MyTheme)
        setContentView(R.layout.app_update_activity)
        appUpdateManager = AppUpdateManagerFactory.create(this)
    }

    override fun onResume() {
        super.onResume()
        findViewById<Button>(R.id.update_button).setOnClickListener {
            val appUpdateInfoTask = appUpdateManager.appUpdateInfo

            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                if (
                    appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    || appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                    && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo, IMMEDIATE, this, 2374987
                    )
                } else {
                    Toast.makeText(this, getString(R.string.no_update_available), Toast.LENGTH_SHORT).show()
                }
            }

            appUpdateInfoTask.addOnFailureListener {
                Log.e(this.javaClass.name, "Failure")
            }
        }

        findViewById<Button>(R.id.postpone_update_button).setOnClickListener {
            val intent = Intent(this, LaunchScreenActivity::class.java)
            startActivity(intent)
        }
    }
}