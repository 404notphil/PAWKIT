package com.tunepruner.fingerperc.zone

import com.tunepruner.fingerperc.instrument.ResourceManager
import com.tunepruner.fingerperc.instrument.ScreenDimensions
import com.tunepruner.fingerperc.zone.zonegraph.V1ZoneGraph
import com.tunepruner.fingerperc.zone.zonegraph.ZoneGraph
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.ArticulationZone
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.V1ArticulationZone
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.V1VelocityZone
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone

class ZoneFactory {
    companion object {
        private const val TAG = "ZoneFactory"
        fun prepareTriggers(
            screenDimensions: ScreenDimensions,
            resourceManager: ResourceManager
        ): ZoneGraph {
            val zoneGraph: ZoneGraph = V1ZoneGraph()
            val articulationCount = resourceManager.getArticulationCount()
            for (articulationNumber in 1..articulationCount) {
                val thisArticulationZone: ArticulationZone =
                    V1ArticulationZone(articulationCount, articulationNumber, screenDimensions)
                val velocityLayerCount = resourceManager.getVelocityLayerCount(articulationNumber)
//                Log.i(
//                    TAG,
//                    "\n\ntriggerzone iteration = ${thisArticulationZone.getArticulationNumber()}\ntop limit = ${thisArticulationZone.getLimits().topLimit}\nbottom limit = ${thisArticulationZone.getLimits().bottomLimit}"
//                )
                for (velocityLayerNumber in 1..velocityLayerCount) {
                    var adjustedVelocityLayerNumber: Int
                    if (articulationNumber == 1) adjustedVelocityLayerNumber = velocityLayerCount - velocityLayerNumber + 1 //So that the first articulation will lay its velocities out in reversed order
                    else adjustedVelocityLayerNumber = velocityLayerNumber
                    val thisVelocityZone: VelocityZone = V1VelocityZone(
                        articulationCount,
                        articulationNumber,
                        adjustedVelocityLayerNumber,
                        velocityLayerNumber,
                        velocityLayerCount,
                        screenDimensions
                    )
//                    Log.i(
//                        TAG,
//                        "\n\nlayer iteration = ${thisVelocityZone.getVelocityNumber()}\ntop limit = ${thisVelocityZone.getLimits().topLimit}\nbottom limit = ${thisVelocityZone.getLimits().bottomLimit}"
//                    )
                    thisArticulationZone.addLayer(thisVelocityZone)

                }
                zoneGraph.addArticulationZone(thisArticulationZone)
            }
            return zoneGraph
        }

    }
}