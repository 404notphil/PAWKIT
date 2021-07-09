package com.tunepruner.fingerperc.sample

import com.tunepruner.fingerperc.instrument.ResourceManager
import com.tunepruner.fingerperc.sample.samplelibrary.SampleLibrary
import com.tunepruner.fingerperc.sample.samplelibrary.V1SampleLibrary
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.Articulation
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.V1Articulation
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.RoundRobinLogic
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.SimpleRoundRobinLogic
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.V1VelocityLayer
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.VelocityLayer
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.BasicCoords
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.Sample
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.SampleCoords
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.V1Sample
import com.tunepruner.fingerperc.zone.zonegraph.ZoneGraph

class SampleFactory {
    companion object {
        fun prepareSamples(zoneGraph: ZoneGraph, resourceManager: ResourceManager): SampleLibrary {
            val sampleLibrary: SampleLibrary = V1SampleLibrary()
            val groupCount = resourceManager.getArticulationCount()
            for (groupIteration in 1..groupCount) {
                val thisArticulation: Articulation = V1Articulation()
                val layerCount = resourceManager.getVelocityLayerCount(groupIteration)
                for (layerIteration in 1..layerCount) {
                    val roundRobinLogic: RoundRobinLogic = SimpleRoundRobinLogic()
                    val thisVelocityLayer: VelocityLayer =
                        V1VelocityLayer(layerIteration, roundRobinLogic)
                    val roundRobinCount =
                        resourceManager.getRoundRobinCount(groupIteration, layerIteration)
                    for (roundRobinIteration in 1..roundRobinCount) {
                        val sampleCoords: SampleCoords = BasicCoords(
                            groupIteration,
                            layerIteration,
                            roundRobinIteration,
                            layerCount,
                            roundRobinCount
                        )

                        val fileSnapshot = resourceManager.getFileSnapshot(
                            groupIteration,
                            layerIteration,
                            roundRobinIteration
                        )
                        val thisRoundRobin: Sample =
                            V1Sample(sampleCoords, fileSnapshot!!)
                        thisVelocityLayer.addSample(sampleCoords, thisRoundRobin)
                        thisVelocityLayer.addSampleCoords(roundRobinIteration, sampleCoords)
                    }
                    thisArticulation.addLayer(
                        zoneGraph.getLayer(groupIteration, layerIteration),
                        thisVelocityLayer
                    )
                }
                sampleLibrary.addSampleGroup(thisArticulation)
            }
            return sampleLibrary
        }

    }
}