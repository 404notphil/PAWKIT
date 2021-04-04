package com.tunepruner.fingerperc.gui

import android.app.Activity
import android.graphics.Point
import android.graphics.PointF
import android.widget.ImageView
import com.tunepruner.fingerperc.R

class InstrumentGUI(private val libraryName: String) {
    lateinit var topArticulationPosition: PointF
    lateinit var bottomArticulationPosition: PointF


    fun setupImages(activity: Activity) {
        activity.setContentView(R.layout.all_in_one_file)

        val topImage = activity.findViewById<ImageView>(R.id.articulation1image)
        val bottomImage = activity.findViewById<ImageView>(R.id.articulation2image)

        if (libraryName == "cajon") {
            topImage.setImageResource(R.mipmap.cajon_top_atrest_foreground)
            bottomImage.setImageResource(R.mipmap.cajon_center_atrest_foreground)
        }
        if (libraryName == "dancedrums") {
            topImage.setImageResource(R.mipmap.rim_png_foreground)
            bottomImage.setImageResource(R.mipmap.head_png_foreground)
            topImage.imageAlpha = 120
            bottomImage.imageAlpha = 120
        }

        activity.findViewById<ImageView>(R.id.up_arrow_image)
            .setImageResource(R.mipmap.up_arrow_foreground)
        activity.findViewById<ImageView>(R.id.up_arrow_image2)
            .setImageResource(R.mipmap.up_arrow_foreground)
        activity.findViewById<ImageView>(R.id.down_arrow_image)
            .setImageResource(R.mipmap.down_arrow_foreground)
        activity.findViewById<ImageView>(R.id.down_arrow_image2)
            .setImageResource(R.mipmap.down_arrow_foreground)

        topArticulationPosition = PointF(topImage.x, topImage.y)
        bottomArticulationPosition = PointF(bottomImage.x, bottomImage.y)

    }
}