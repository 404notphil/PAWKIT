package com.tunepruner.bomboleguerodemo.graphics

import com.tunepruner.bomboleguerodemo.instrument.ResourceManager
import com.tunepruner.bomboleguerodemo.trigger.triggergraph.triggerzone.zonelayer.LayerZone
import java.util.*

class SimpleGUIManager(resourceManager: ResourceManager) : GUIManager {
    var restartRequested = true
    var isAnimating = false
    val zones: LinkedList<LinkedList<Cycle>> = LinkedList<LinkedList<Cycle>>()
//    var standardImage: Int

    init {
//        standardImage = 0//TODO add real resource here
//
//
//        for (i in 1..resourceManager.getGroupCount()){
//            val zone = LinkedList<Cycle>()
//            zones.add(zone)
//            for (j in 1..resourceManager.getLayerCount(i)){
//                val cycle = Cycle(0, 0)//TODO add real resources here
//                if (j > 1) zone[j].subcycle = cycle
//            }
//        }
        //an init function that calls ResourceManager to get info on zones and layers and populates each List<Cycle> in zones
            //it creates an array of four cycles (because there are four image sets)
                //and it decides how to pass those out to the layers.
    }


    override fun startAnimation(layerZone: LayerZone){
//        if (isAnimating) restartRequested = true
//        else isAnimating = true
//
//
//        val zoneNumber = layerZone.getZoneIteration()
//        val layerNumber = layerZone.getLayerIteration()
//        val cycleZone = zones[zoneNumber]
//        val cycle = cycleZone[layerNumber]
//        cycle.cycle()
//
//
//        //At end...
//        isAnimating = false
//        restartRequested = false //Watch out for interminable loops
    }

    private fun setUpResources(): IntArray{
        val resources = IntArray(4)
//        resources[0] = 0//TODO add real resources here
//        resources[1] = 0
//        resources[2] = 0
//        resources[3] = 0
        return resources
    }

}

class Cycle(val smallVariationImage: Int, val largeVariationImage: Int){
    var subcycle: Cycle? = null
    fun cycle() {
        //code to set timers and switch between variation based on them
        subcycle?.cycle()
    }

}