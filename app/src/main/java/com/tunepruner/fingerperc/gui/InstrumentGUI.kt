package com.tunepruner.fingerperc.gui

import android.app.Activity
import android.widget.ImageView
import com.tunepruner.fingerperc.R

class InstrumentGUI(val activity: Activity, val libraryName: String) {
    fun setupImages() {
        activity.setContentView(R.layout.all_in_one_file)
        if (libraryName == "cajon") {
            activity.findViewById<ImageView>(R.id.articulation1image)
                .setImageResource(R.mipmap.cajon_top_atrest_foreground)
            activity.findViewById<ImageView>(R.id.articulation2image)
                .setImageResource(R.mipmap.cajon_center_atrest_foreground)
        }
        if (libraryName == "bomboleguero") {
            activity.findViewById<ImageView>(R.id.articulation1image)
                .setImageResource(R.mipmap.rim_png_foreground)
            activity.findViewById<ImageView>(R.id.articulation2image)
                .setImageResource(R.mipmap.head_png_foreground)
        }

        activity.findViewById<ImageView>(R.id.up_arrow_image)
            .setImageResource(R.mipmap.up_arrow_foreground)
        activity.findViewById<ImageView>(R.id.up_arrow_image2)
            .setImageResource(R.mipmap.up_arrow_foreground)
        activity.findViewById<ImageView>(R.id.down_arrow_image)
            .setImageResource(R.mipmap.down_arrow_foreground)
        activity.findViewById<ImageView>(R.id.down_arrow_image2)
            .setImageResource(R.mipmap.down_arrow_foreground)
    }
}