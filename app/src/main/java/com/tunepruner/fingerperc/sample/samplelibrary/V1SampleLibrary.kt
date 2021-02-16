package com.tunepruner.fingerperc.sample.samplelibrary

import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.SampleGroup
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.Playable
import com.tunepruner.fingerperc.trigger.triggergraph.triggerzone.zonelayer.LayerZone
import java.util.*

class V1SampleLibrary : SampleLibrary {
    val groups: LinkedList<SampleGroup> = LinkedList()

    override fun computeSample(layerZone: LayerZone): Playable {
        val currentGroup = groups[layerZone.getZoneIteration()-1]
        return currentGroup.invokeLayer(layerZone)
    }


    override fun addSampleGroup(sampleGroup: SampleGroup) {
        groups.add(sampleGroup)
    }
}