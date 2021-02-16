package com.tunepruner.bomboleguerodemo.trigger

import android.util.Log
import com.tunepruner.bomboleguerodemo.instrument.ResourceManager
import com.tunepruner.bomboleguerodemo.instrument.ScreenDimensions
import com.tunepruner.bomboleguerodemo.trigger.triggergraph.TriggerGraph
import com.tunepruner.bomboleguerodemo.trigger.triggergraph.V1TriggerGraph
import com.tunepruner.bomboleguerodemo.trigger.triggergraph.triggerzone.TriggerZone
import com.tunepruner.bomboleguerodemo.trigger.triggergraph.triggerzone.V1TriggerZone
import com.tunepruner.bomboleguerodemo.trigger.triggergraph.triggerzone.zonelayer.V1LayerZone
import com.tunepruner.bomboleguerodemo.trigger.triggergraph.triggerzone.zonelayer.LayerZone

class TriggerGraphFactory {
    companion object {
        fun prepareTriggers(screenDimensions: ScreenDimensions, resourceManager: ResourceManager): TriggerGraph {
            val triggerGraph: TriggerGraph = V1TriggerGraph()
            val zoneCount = resourceManager.getGroupCount()
            for (zoneIteration in 1..zoneCount) {
                val thisTriggerTriggerZone: TriggerZone = V1TriggerZone(zoneCount, zoneIteration, screenDimensions)
                val layerCount = resourceManager.getLayerCount(zoneIteration)
                for (layerIteration in 1..layerCount) {
                    val thisLayerZone: LayerZone = V1LayerZone(zoneCount, zoneIteration, layerIteration, layerCount, screenDimensions)
                    Log.i("TriggerGraphFactory",
                        "layer iteration = ${thisLayerZone.getLayerIteration()}\ntop limit = ${thisLayerZone.getLimits().topLimit}\nbottom limit = ${thisLayerZone.getLimits().bottomLimit}")
                    thisTriggerTriggerZone.addLayer(thisLayerZone)
                }
                triggerGraph.addTriggerZone(thisTriggerTriggerZone)
            }
            return triggerGraph
        }

    }
}