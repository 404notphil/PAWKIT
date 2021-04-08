package com.tunepruner.fingerperc.gui

import android.app.Activity
import android.graphics.PointF
import android.widget.ImageView
import android.widget.TextView
import com.tunepruner.fingerperc.R

class InstrumentGUI(private val libraryName: String) {
    lateinit var topArticulationPosition: PointF
    lateinit var bottomArticulationPosition: PointF


    fun setupImages(activity: Activity) {
        activity.setContentView(R.layout.all_in_one_file)

        val topImage = activity.findViewById<ImageView>(R.id.articulation1image)
        val bottomImage = activity.findViewById<ImageView>(R.id.articulation2image)

        if (libraryName == "cajon") {
            topImage.setImageResource(R.drawable.cajon_high_atrest)
            bottomImage.setImageResource(R.drawable.cajon_low_atrest)
        }
        if (libraryName == "dancedrums") {
            topImage.setImageResource(R.drawable.dancedrums_high_atrest)
            bottomImage.setImageResource(R.drawable.dancedrums_low_atrest)
            topImage.imageAlpha = 120
            bottomImage.imageAlpha = 120
        }
        if(libraryName == "bomboleguero"){
            topImage.setImageResource(R.drawable.bomboleguero_high_atrest)
            bottomImage.setImageResource(R.drawable.bomboleguero_low_atrest)
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

    fun setUpTitles(activity: Activity) {
        val topTitle = activity.findViewById<TextView>(R.id.articulation1title)
        val bottomTitle = activity.findViewById<TextView>(R.id.articulation2title)

        if (libraryName == "cajon") {
            topTitle.setText("Edge Sound")
            bottomTitle.setText("Center Sound")
        }
        if (libraryName == "dancedrums") {
            topTitle.setText("Snare")
            bottomTitle.setText("Kick")
        }
        if(libraryName == "bomboleguero"){
            topTitle.setText("Rim Sound")
            bottomTitle.setText("Center Sound")
        }

    }
}