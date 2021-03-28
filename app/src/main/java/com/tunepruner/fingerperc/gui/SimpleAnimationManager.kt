package com.tunepruner.fingerperc.gui

import android.app.Activity
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.instrument.ResourceManager
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class SimpleAnimationManager(val resourceManager: ResourceManager, val activity: Activity) :
    AnimationManager {
    val TAG: String = "AniManager"
    val articulationArrays: ArrayList<ArrayList<Animation>> = ArrayList()

    init {
        articulationArrays.add(ArrayList())
        articulationArrays.add(ArrayList())
    }


    override fun animate(velocityZone: VelocityZone) {
        Log.i(TAG, "size 0 = ${articulationArrays[0].size}")
        Log.i(TAG, "size 1 = ${articulationArrays[1].size}")
        stopAllPrevious(velocityZone.getArticulationNumber())
        val animation = Animation(this, velocityZone, resourceManager, activity)
        articulationArrays[velocityZone.getArticulationNumber() - 1].add(animation)
    }

    private fun stopAllPrevious(articulationNumber: Int) {
        Log.i(TAG, "stopAllPrevious")

        if (!articulationArrays.isNullOrEmpty())
            for (element in articulationArrays[articulationNumber - 1]) {
                element.stopAnimation()
            }
    }
}

class Animation(
    val animationManager: SimpleAnimationManager,
    val velocityZone: VelocityZone,
    val resourceManager: ResourceManager,
    val activity: Activity
) {
    val TAG: String = "Animation.Class"
    private val offsetMax = 25
    private val offsetMin = 5
    private val durationMax: Long = 500
    private val articulationNumber = velocityZone.getArticulationNumber()
    private var stopRequested: Boolean = false

    init {
        val imageID = findImageToAnimate(velocityZone)
        val textID = findTextToAnimate(velocityZone)


        val velocityCount =
            resourceManager.getVelocityLayerCount(articulationNumber)
        val velocityNumber = velocityZone.getVelocityNumber()

        val offset = (offsetMax / velocityCount) * velocityNumber
        val duration = (durationMax / velocityCount) * velocityNumber

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
        Log.i(TAG, "stopRequested")

    }

    private fun startAnimation(
        offset: Int,
        duration: Long,
        imageID: Int,
        textID: Int,
        articulationNumber: Int
    ) {


        GlobalScope.launch {
            var counter = 0
            var remaining = offset
            val origin = activity.findViewById<ImageView>(imageID).x

            while (counter < duration &&
                remaining > offsetMin
            ) {

                if (stopRequested) break

                activity.findViewById<ImageView>(imageID).imageAlpha = 255
                activity.findViewById<TextView>(textID).alpha = 0.05F

                activity.findViewById<ImageView>(imageID).x = origin + remaining
                delay(6)

                activity.findViewById<ImageView>(imageID).y = origin + remaining
                delay(6)

                activity.findViewById<ImageView>(imageID).x = origin - remaining
                delay(6)

                activity.findViewById<ImageView>(imageID).y = origin - remaining
                delay(6)

                activity.findViewById<ImageView>(imageID).x = origin
                delay(6)

                activity.findViewById<ImageView>(imageID).y = origin

                counter += 36
                remaining -= 2

            }
            if (!stopRequested) {
                delay(300)
                activity.findViewById<ImageView>(imageID).imageAlpha = 120
                activity.findViewById<TextView>(textID).alpha = .3F
            }
        }

        Log.i(TAG, "size before delete= ${animationManager.articulationArrays[articulationNumber - 1].size}")
        animationManager.articulationArrays[articulationNumber - 1].remove(this)
        Log.i(TAG, "size after delete= ${animationManager.articulationArrays[articulationNumber - 1].size}")
        stopRequested = false

    }
}

