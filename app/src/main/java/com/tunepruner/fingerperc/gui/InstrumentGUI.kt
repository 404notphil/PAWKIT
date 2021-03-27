package com.tunepruner.fingerperc.gui

import android.app.Activity
import android.widget.ImageView
import com.tunepruner.fingerperc.R

class InstrumentGUI(val activity: Activity, val libraryName: String) {
    fun setupImages(){
        activity.setContentView(R.layout.playable_area)
        if (libraryName == "cajon") {
            activity.findViewById<ImageView>(R.id.rimImage).setImageResource(R.mipmap.cajon_top_atrest_foreground)
            activity.findViewById<ImageView>(R.id.headImage).setImageResource(R.mipmap.cajon_center_atrest_foreground)
        }
    }
}