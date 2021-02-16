package com.tunepruner.fingerperc.sample.samplelibrary

import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.SampleGroup
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.Playable
import com.tunepruner.fingerperc.trigger.triggergraph.triggerzone.zonelayer.LayerZone

interface SampleLibrary {
    fun computeSample(layerZone: LayerZone): Playable
    fun addSampleGroup(sampleGroup: SampleGroup)
}