package com.tunepruner.fingerperc.sample.samplelibrary.samplegroup

import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.SampleLayer
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.Playable
import com.tunepruner.fingerperc.trigger.triggergraph.triggerzone.zonelayer.LayerZone

interface SampleGroup {
    fun invokeLayer(layerZone: LayerZone): Playable
    fun addLayer(layerZone: LayerZone, sampleLayer: SampleLayer)
}