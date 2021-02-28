package com.tunepruner.fingerperc.zone

import android.util.Log
import com.tunepruner.fingerperc.instrument.ResourceManager
import com.tunepruner.fingerperc.instrument.ScreenDimensions
import com.tunepruner.fingerperc.zone.zonegraph.ZoneGraph
import com.tunepruner.fingerperc.zone.zonegraph.V1ZoneGraph
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.ArticulationZone
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.V1ArticulationZone
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.V1VelocityZone
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone

class ZoneFactory {
    companion object {
        private const val TAG = "ZoneFactory"
        fun prepareTriggers(screenDimensions: ScreenDimensions, resourceManager: ResourceManager): ZoneGraph {
            val zoneGraph: ZoneGraph = V1ZoneGraph()
            val zoneCount = resourceManager.getArticulationCount()
            for (zoneIteration in 1..zoneCount) {
                val thisArticulationZone: ArticulationZone = V1ArticulationZone(zoneCount, zoneIteration, screenDimensions)
                val layerCount = resourceManager.getVelocityLayerCount(zoneIteration)
                Log.i(TAG,
                    "\n\ntriggerzone iteration = ${thisArticulationZone.getArticulationNumber()}\ntop limit = ${thisArticulationZone.getLimits().topLimit}\nbottom limit = ${thisArticulationZone.getLimits().bottomLimit}")
                for (layerIteration in 1..layerCount) {
                    val thisVelocityZone: VelocityZone = V1VelocityZone(zoneCount, zoneIteration, layerIteration, layerCount, screenDimensions)
                    Log.i(TAG,
                        "\n\nlayer iteration = ${thisVelocityZone.getVelocityNumber()}\ntop limit = ${thisVelocityZone.getLimits().topLimit}\nbottom limit = ${thisVelocityZone.getLimits().bottomLimit}")
                    thisArticulationZone.addLayer(thisVelocityZone)
                }
                zoneGraph.addArticulationZone(thisArticulationZone)
            }
            return zoneGraph
        }

    }
}