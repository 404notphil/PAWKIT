package com.tunepruner.fingerperc.gui

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.instrument.zone.zonegraph.articulationzone.velocityzone.VelocityZone

class AnimationOfTitle {
    companion object {
        private val arrayDeque1: ArrayDeque<AnimationOfTitleRequest> = ArrayDeque()
        private val arrayDeque2: ArrayDeque<AnimationOfTitleRequest> = ArrayDeque()

        fun addRequest(request: AnimationOfTitleRequest, articulationNumber: Int) {
            if (articulationNumber == 1) {
                arrayDeque1.add(request)
            } else {
                arrayDeque2.add(request)
            }
        }

        fun removeRequest(articulationNumber: Int) {
            if (articulationNumber == 1) arrayDeque1.removeFirstOrNull() else arrayDeque2.removeFirstOrNull()
        }

        fun isEmpty(articulationNumber: Int): Boolean {
            return if (articulationNumber == 1) arrayDeque1.isEmpty() else arrayDeque2.isEmpty()
        }
    }
}

class AnimationOfTitleRequest(activity: Activity, val velocityZone: VelocityZone) {
    private val articulationNumber = velocityZone.getArticulationNumber()
    private val imageView: ImageView = if (velocityZone.getArticulationNumber() == 1) {
        activity.findViewById(R.id.articulation1image)
    } else {
        activity.findViewById(R.id.articulation2image)
    }
    private val textView: TextView = if (velocityZone.getArticulationNumber() == 1) {
        activity.findViewById(R.id.articulation1title)
    } else {
        activity.findViewById(R.id.articulation2title)
    }

    init {
        AnimationOfTitle.addRequest(this, articulationNumber)
        bringGraphicForward()
        sendGraphicToBack()

    }

    private fun bringGraphicForward() {
        imageView.imageAlpha = 255
        textView.alpha = 0.0F
    }

    private fun sendGraphicToBack() {
        Handler(Looper.getMainLooper()).postDelayed({
            AnimationOfTitle.removeRequest(articulationNumber)
            if (AnimationOfTitle.isEmpty(articulationNumber)) {
                imageView.imageAlpha = 50
                textView.alpha = 0.3F
            }
        }, 2000)


        //Todo finish animating!
    }
}