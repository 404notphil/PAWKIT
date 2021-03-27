package com.tunepruner.fingerperc.gui

import android.widget.ImageView
import com.tunepruner.fingerperc.instrument.ResourceManager
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone

class SimpleAnimationManager(val resourceManager: ResourceManager) : AnimationManager {
    val OFFSET_MAX = 30
    val OFFSET_MIN = 5
    val DURATION_MAX: Long = 1000
    val DURATION_MIN: Long = 100

    override fun animate(velocityZone: VelocityZone) {
        val image = findImageToAnimate(velocityZone)

        val velocityCount =
            resourceManager.getVelocityLayerCount(velocityZone.getArticulationNumber())
        val velocityNumber = velocityZone.getVelocityNumber()

        val offset = (OFFSET_MAX / velocityCount) * velocityNumber
        val duration = (DURATION_MAX / velocityCount) * velocityNumber

        startAnimation(offset, duration, image)
    }

    private fun findImageToAnimate(velocityZone: VelocityZone): ImageView {
//        return if (velocityZone.getArticulationNumber() == 1) {
//            //articulation1 from layout
//        } else {
//            //articulation2 from layout
//        }
    }

    fun startAnimation(offset: Int, duration: Long, image: ImageView) {
        //todo implement

    }
}

