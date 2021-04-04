package com.tunepruner.fingerperc.instrument

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Insets
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.WindowInsets

class ScreenPrep {


    //Code copied from thread : / oops
    // https://stackoverflow.com/questions/63407883/getting-screen-width-on-api-level-30-android-11-getdefaultdisplay-and-getme
    companion object {
        val TAG = "ScreenPrep"
        private fun getWidth(activity: Activity): Int {
            val thisVersion = Build.VERSION.SDK_INT
            return if (thisVersion >= Build.VERSION_CODES.R) {
                val windowMetrics = activity.windowManager.currentWindowMetrics
                val insets: Insets = windowMetrics.windowInsets
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                windowMetrics.bounds.width() - insets.left - insets.right
            } else {
                val displayMetrics = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
                displayMetrics.widthPixels
            }
        }

        private fun getHeight(activity: Activity): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {//changed from R to Q for testing
                val windowMetrics = activity.windowManager.currentWindowMetrics
                /*val insets: Insets = windowMetrics.windowInsets
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())*/
//                Log.i(TAG, "screen height = ${windowMetrics.bounds.height()}")
                windowMetrics.bounds.height()/* - insets.top - insets.bottom*/
            } else {
                val displayMetrics = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
//                Log.i(TAG, "screen height DIP= ${displayMetrics.heightPixels}")
//                Log.i(TAG, "screen height raw= ${ceil(displayMetrics.heightPixels * (displayMetrics.densityDpi / displayMetrics.density)).toInt()}")
                var density = displayMetrics.density
                var densityDIP = displayMetrics.densityDpi
                val heightPixels = displayMetrics.heightPixels

//                var heightToReturn = ceil(displayMetrics.heightPixels * (displayMetrics.densityDpi / 160.0)).toInt()
//                displayMetrics.heightPixels
                heightPixels
            }
        }

        fun getDimensions(activity: Activity): ScreenDimensions {
            val height = getHeight(activity)
            val width = getWidth(activity)

            return ScreenDimensions(height, width)
        }
    }
}

data class ScreenDimensions(val screenHeight: Int, val screenWidth: Int)