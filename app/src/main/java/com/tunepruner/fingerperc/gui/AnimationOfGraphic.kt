package com.tunepruner.fingerperc.gui

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.instrument.ResourceManager
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone
import kotlin.math.roundToInt

class AnimationOfGraphic(
    private val animationManager: SimpleAnimationManager,
    val velocityZone: VelocityZone,
    resourceManager: ResourceManager,
    private val activity: Activity,
    instrumentGUI: InstrumentGUI,
    private val currentIndex: Int
) {
    val TAG: String = "AnimationOfGraphic.Class"
    private val offsetMax = 30
    private val durationMax = 700L
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

        val offset = (offsetMax / velocityCount) * velocityNumber + 10
        val duration = (durationMax / velocityCount) * velocityNumber

        if (articulationNumber == 1) {
            originX = instrumentGUI.topArticulationPosition.x
            originY = instrumentGUI.topArticulationPosition.y
        } else {
            originX = instrumentGUI.bottomArticulationPosition.x
            originY = instrumentGUI.bottomArticulationPosition.y
        }

        startAnimation(offset, duration, imageID, articulationNumber)
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
        articulationNumber: Int
    ) {
        var cycleStart = 0L
        var counter = 0
        var adjustedCoordsOffset = offset

        val imageView = activity.findViewById<ImageView>(imageID)
        val delayPartial = 5
        val cycleLength = delayPartial * 5
        val totalCycles = duration / cycleLength
        val constant = 100
        val numeratorBase = 95
        val numeratorIncrement: Long = (constant - numeratorBase) / totalCycles
        /*As such, after the first cycle, the coords offset will be reset to itself multiplied by 95/100, the following cycle by 96/100, etc */
        val ratio: Double = (numeratorBase + counter.times(numeratorIncrement).toDouble()) / constant

        imageView.scaleX = 1F + (velocityZone.getVelocityNumber() * 0.2F / velocityZone.getVelocityCount())
        imageView.scaleY = 1F + (velocityZone.getVelocityNumber() * 0.2F / velocityZone.getVelocityCount())
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            imageView.scaleX = 1.0F
            imageView.scaleY = 1.0F
        }, 50)

        while (
            cycleStart < duration &&
            !stopRequested
        ) {
            val adjustedOffsetLocal = adjustedCoordsOffset
            val delayLocal = cycleStart


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
                                }, delayPartial.toLong())
                            }, delayPartial.toLong())
                        }, delayPartial.toLong())
                    }, delayPartial.toLong())
                }, delayPartial.toLong())
            }, delayLocal)

            cycleStart += cycleLength
            counter++
            adjustedCoordsOffset = (adjustedCoordsOffset * ratio).roundToInt()
        }

        animationManager.articulationArrays[articulationNumber - 1].remove(currentIndex - 1)

        stopRequested = false

    }

}