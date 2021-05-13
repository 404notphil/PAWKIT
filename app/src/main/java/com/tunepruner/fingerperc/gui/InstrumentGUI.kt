package com.tunepruner.fingerperc.gui

import android.app.Activity
import android.graphics.PointF
import android.widget.ImageView
import android.widget.TextView
import com.tunepruner.fingerperc.R

class InstrumentGUI(val libraryID: String) {
    lateinit var topArticulationPosition: PointF
    lateinit var bottomArticulationPosition: PointF


    fun setupImages(activity: Activity) {
        activity.setContentView(R.layout.all_in_one_file)

        val topImage = activity.findViewById<ImageView>(R.id.articulation1image)
        val bottomImage = activity.findViewById<ImageView>(R.id.articulation2image)

        when (libraryID) {
            "cajon" -> {
                topImage.setImageResource(R.drawable.cajon_high_atrest)
                bottomImage.setImageResource(R.drawable.cajon_low_atrest)
                topImage.imageAlpha = 50
                bottomImage.imageAlpha = 50
            }
            "bomboleguero" -> {
                topImage.setImageResource(R.drawable.bomboleguero_high_atrest)
                bottomImage.setImageResource(R.drawable.bomboleguero_low_atrest)
                topImage.imageAlpha = 50
                bottomImage.imageAlpha = 50
            }
            else -> {
                topImage.setImageResource(R.drawable.dancedrums_high_atrest)
                bottomImage.setImageResource(R.drawable.dancedrums_low_atrest)
                topImage.imageAlpha = 50
                bottomImage.imageAlpha = 50
            }
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

        when (libraryID) {
            "cajon" -> {
                topTitle.text = activity.getString(R.string.cajonTopArticulationTitle)
                bottomTitle.text = activity.getString(R.string.cajonBottomArticulationTitle)
            }
            "bomboleguero" -> {
                topTitle.text = activity.getString(R.string.bomboTopArticulationTitle)
                bottomTitle.text = activity.getString(R.string.bomboBottomArticulationTitle)
            }
            else -> {
                topTitle.text = activity.getString(R.string.drumTopArticulationTitle)
                bottomTitle.text = activity.getString(R.string.drumBottomArticulationTitle)
            }

        }
    }
}
