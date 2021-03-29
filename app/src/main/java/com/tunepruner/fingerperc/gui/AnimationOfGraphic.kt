package com.tunepruner.fingerperc.gui

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.instrument.ResourceManager
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone

class AnimationOfGraphic(
    private val animationManager: SimpleAnimationManager,
    velocityZone: VelocityZone,
    resourceManager: ResourceManager,
    private val activity: Activity,
    instrumentGUI: InstrumentGUI,
    private val currentIndex: Int
) {
    val TAG: String = "AnimationOfGraphic.Class"
    private val offsetMax = 50
    private val offsetMin = 5
    private val durationMax = 3000L
    private val articulationNumber = velocityZone.getArticulationNumber()
    private var stopRequested: Boolean = false
    private var originX: Float
    private var originY: Float

    init {
        val imageID = findImageToAnimate(velocityZone)
        val textID = findTextToAnimate(velocityZone)

        val velocityCount =
            resourceManager.getVelocityLayerCount(articulationNumber)
        val velocityNumber = velocityZone.getVelocityNumber()

        val offset = (offsetMax / velocityCount) * velocityNumber
        val duration = (durationMax / velocityCount) * velocityNumber

        if (articulationNumber == 1) {
            originX = instrumentGUI.topArticulationPosition.x
            originY = instrumentGUI.topArticulationPosition.y
        } else {
            originX = instrumentGUI.bottomArticulationPosition.x
            originY = instrumentGUI.bottomArticulationPosition.y
        }

        startAnimation(offset, duration, imageID, textID, articulationNumber)
    }

    private fun findImageToAnimate(velocityZone: VelocityZone): Int {
        return if (velocityZone.getArticulationNumber() == 1) {
            R.id.articulation1image
        } else {
            R.id.articulation2image
        }
    }

    private fun findTextToAnimate(velocityZone: VelocityZone): Int {
        return if (velocityZone.getArticulationNumber() == 1) {
            R.id.articulation1title
        } else {
            R.id.articulation2title
        }
    }

    fun stopAnimation() {
        stopRequested = true
    }

    private fun startAnimation(
        offset: Int,
        duration: Long,
        imageID: Int,
        textID: Int,
        articulationNumber: Int
    ) {
        var timeSpent = 0
        var adjustedOffset = offset
        var delay = 0L
        var counter = 0
        val imageView = activity.findViewById<ImageView>(imageID)

        while (
            timeSpent < duration &&
            adjustedOffset > offsetMin &&
            !stopRequested
        ) {
            val handler = Handler(Looper.getMainLooper())
            val adjustedOffsetLocal = adjustedOffset
            val delayLocal = delay

            handler.postDelayed({
                imageView.x = originX + adjustedOffsetLocal
                handler.postDelayed({
                    imageView.y = originY + adjustedOffsetLocal
                    handler.postDelayed({
                        imageView.y = originY - adjustedOffsetLocal
                        handler.postDelayed({
                            imageView.x = originX - adjustedOffsetLocal
                            handler.postDelayed({
                                imageView.x = originX
                                handler.postDelayed({
                                    imageView.y = originY
                                }, 5)
                            }, 5)
                        }, 5)
                    }, 5)
                }, 5)
            }, delayLocal)

            timeSpent += 30
            adjustedOffset = if (counter < 2) adjustedOffset - 15 else adjustedOffset - 1
            delay += 30
            counter++

        }

        animationManager.articulationArrays[articulationNumber - 1].remove(currentIndex - 1)

        stopRequested = false

    }

}