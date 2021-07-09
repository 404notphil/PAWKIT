package com.tunepruner.fingerperc.gui

import android.app.Activity
import android.util.Log
import com.tunepruner.fingerperc.instrument.ResourceManager
import com.tunepruner.fingerperc.instrument.zone.zonegraph.articulationzone.velocityzone.VelocityZone

class SimpleAnimationManager(
    val resourceManager: ResourceManager,
    val instrumentGUI: InstrumentGUI
) :
    AnimationManager {
    val TAG: String = "AniManager"
    val articulationArrays: ArrayList<HashMap<Int, AnimationOfGraphic>> = ArrayList()
    var currentIndex = 0

    init {
        articulationArrays.add(HashMap())
        articulationArrays.add(HashMap())
    }


    override fun animate(velocityZone: VelocityZone, activity: Activity) {
        Log.i(TAG, "size 0 = ${articulationArrays[0].size}")
        Log.i(TAG, "size 1 = ${articulationArrays[1].size}")

        articulationArrays[velocityZone.getArticulationNumber() - 1][currentIndex]?.stopAnimation() /*Stop the previous animation*/

        val animationOfTitleRequest = AnimationOfTitleRequest(activity, velocityZone)
        val animationOfGraphic =
            AnimationOfGraphic(this, velocityZone, resourceManager, instrumentGUI, currentIndex, activity)

        articulationArrays[velocityZone.getArticulationNumber() - 1][currentIndex] = animationOfGraphic
        currentIndex++
    }

}



