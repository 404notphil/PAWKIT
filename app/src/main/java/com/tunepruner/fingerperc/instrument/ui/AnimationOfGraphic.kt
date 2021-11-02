package com.tunepruner.fingerperc.instrument.ui

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.instrument.ResourceManager
import com.tunepruner.fingerperc.instrument.zone.zonegraph.articulationzone.velocityzone.VelocityZone
import kotlin.math.roundToInt

class AnimationOfGraphic(
    private val animationManager: SimpleAnimationManager,
    val velocityZone: VelocityZone,
    resourceManager: ResourceManager,
    val instrumentGUI: InstrumentGUI,
    private val currentIndex: Int,
    activity: Activity
) {
    val TAG: String = "AnimOfGrphc.Class"
    private val offsetMax = 40
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
        val duration = (durationMax / velocityCount) * velocityNumber + 100

        if (articulationNumber == 1) {
            originX = instrumentGUI.topArticulationPosition.x
            originY = instrumentGUI.topArticulationPosition.y
        } else {
            originX = instrumentGUI.bottomArticulationPosition.x
            originY = instrumentGUI.bottomArticulationPosition.y
        }

        startAnimation(offset, duration, imageID, articulationNumber, activity)
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
        articulationNumber: Int,
        activity: Activity
    ) {
        var cycleStart = 0L
        var counter = 0
        var adjustedCoordsOffset = offset

        val imageView = activity.findViewById<ImageView>(imageID)
        val delayPartial = 5
        val cycleLength = delayPartial * 5
        val totalCycles = duration / cycleLength
        Log.i(TAG, "totalCycles = $totalCycles")
        val constant = 100
        val numeratorBase = 85
        val numeratorIncrement: Long = (constant - numeratorBase) / totalCycles
        /*As such, after the first cycle, the coords offset will be reset to itself multiplied by 70/100, the following cycle by 96/100, etc */
        val ratio: Double =
            (numeratorBase + counter.times(numeratorIncrement).toDouble()) / constant

        val handler = Handler(Looper.getMainLooper())
        onHitImageSwap(imageView, handler)

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
                        imageView.translationY = originY - adjustedOffsetLocal
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

            Log.i(TAG, "$adjustedCoordsOffset")

            cycleStart += cycleLength
            counter++
            adjustedCoordsOffset = (adjustedCoordsOffset * ratio).toFloat().roundToInt()
        }

        animationManager.articulationArrays[articulationNumber - 1].remove(currentIndex - 1)

        stopRequested = false
    }

    private fun onHitImageSwap(imageView: ImageView, handler: Handler) {
        val onHitImage =
            if (instrumentGUI.libraryID == "cajon") {
                if (articulationNumber == 1) {
                    R.drawable.cajon_high_onhit
                } else R.drawable.cajon_low_onhit
            }else if (instrumentGUI.libraryID == "bomboleguero"){
                if (articulationNumber == 1) {
                    R.drawable.bomboleguero_high_onhit
                } else R.drawable.bomboleguero_low_onhit
            }else {
                if (articulationNumber == 1) {
                    R.drawable.dancedrums_high_onhit
                } else R.drawable.dancedrums_low_onhit
            }

        val atRestImage =
            if (instrumentGUI.libraryID == "cajon") {
                if (articulationNumber == 1) {
                    R.drawable.cajon_high_atrest
                } else R.drawable.cajon_low_atrest
            }else if (instrumentGUI.libraryID == "bomboleguero"){
                if (articulationNumber == 1) {
                    R.drawable.bomboleguero_high_atrest
                } else R.drawable.bomboleguero_low_atrest
            }else {
                if (articulationNumber == 1) {
                    R.drawable.dancedrums_high_atrest
                } else R.drawable.dancedrums_low_atrest
            }

            imageView.setImageResource(onHitImage)
            handler.postDelayed({
                imageView.setImageResource(atRestImage)
            }, 120)

    }

}