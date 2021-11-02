package com.tunepruner.fingerperc.instrument.ui

import android.app.Activity
import com.tunepruner.fingerperc.instrument.ResourceManager
import com.tunepruner.fingerperc.instrument.zone.zonegraph.articulationzone.velocityzone.VelocityZone

class SimpleAnimationManager(
    val resourceManager: ResourceManager,
    val instrumentGUI: InstrumentGUI
) :
    AnimationManager {
    val articulationArrays: ArrayList<HashMap<Int, AnimationOfGraphic>> = ArrayList()
    var currentIndex = 0

    init {
        articulationArrays.add(HashMap())
        articulationArrays.add(HashMap())
    }


    override fun animate(velocityZone: VelocityZone, activity: Activity) {
        articulationArrays[velocityZone.getArticulationNumber() - 1][currentIndex]?.stopAnimation() /*Stop the previous animation*/

        AnimationOfTitleRequest(activity, velocityZone)
        val animationOfGraphic =
            AnimationOfGraphic(this, velocityZone, resourceManager, instrumentGUI, currentIndex, activity)

        articulationArrays[velocityZone.getArticulationNumber() - 1][currentIndex] = animationOfGraphic
        currentIndex++
    }

}



