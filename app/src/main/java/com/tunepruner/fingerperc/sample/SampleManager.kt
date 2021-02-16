package com.tunepruner.fingerperc.sample

import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.Playable
import com.tunepruner.fingerperc.trigger.triggergraph.triggerzone.zonelayer.LayerZone

interface SampleManager {
    fun computeSample(layerZone: LayerZone): Playable
}