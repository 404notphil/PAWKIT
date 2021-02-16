package com.tunepruner.fingerperc.sample

import com.tunepruner.fingerperc.instrument.ResourceManager
import com.tunepruner.fingerperc.sample.samplelibrary.SampleLibrary
import com.tunepruner.fingerperc.sample.samplelibrary.V1SampleLibrary
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.SampleGroup
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.V1SampleGroup
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.RoundRobinLogic
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.SampleLayer
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.SimpleRoundRobinLogic
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.V1SampleLayer
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.Playable
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.BasicCoords
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.SampleCoords
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.V1Sample
import com.tunepruner.fingerperc.trigger.triggergraph.TriggerGraph

class SampleLibraryFactory {
    companion object {
        fun prepareSamples(triggerGraph: TriggerGraph, resourceManager: ResourceManager): SampleLibrary {
            val sampleLibrary: SampleLibrary = V1SampleLibrary()
            val groupCount = resourceManager.getGroupCount()
            for (groupIteration in 1..groupCount) {
                val thisSampleGroup: SampleGroup = V1SampleGroup()
                val layerCount = resourceManager.getLayerCount(groupIteration)
                for (layerIteration in 1..layerCount) {
                    val roundRobinLogic: RoundRobinLogic = SimpleRoundRobinLogic()
                    val thisSampleLayer: SampleLayer =
                        V1SampleLayer(layerIteration, roundRobinLogic)
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
                        val assetFileDescriptor = resourceManager.getAssetFileDescriptor(
                            groupIteration,
                            layerIteration,
                            roundRobinIteration
                        )//TODO delete this last call, the method it calls, and the property it sets after the transition to Oboe!
                        val fileSnapshot = resourceManager.getFileSnapshot(
                            groupIteration,
                            layerIteration,
                            roundRobinIteration
                        )
                        val thisRoundRobin: Playable =
                            V1Sample(sampleCoords, assetFileDescriptor!!, fileSnapshot!!)
                        thisSampleLayer.addPlayable(sampleCoords, thisRoundRobin)
                        thisSampleLayer.addSampleCoords(roundRobinIteration, sampleCoords)
                    }
                    thisSampleGroup.addLayer(
                        triggerGraph.getLayer(groupIteration, layerIteration),
                        thisSampleLayer
                    )
                }
                sampleLibrary.addSampleGroup(thisSampleGroup)
            }
            return sampleLibrary
        }

    }
}