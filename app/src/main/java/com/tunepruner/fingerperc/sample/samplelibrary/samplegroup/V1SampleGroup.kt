package com.tunepruner.fingerperc.sample.samplelibrary.samplegroup

import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.SampleLayer
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.Playable
import com.tunepruner.fingerperc.trigger.triggergraph.triggerzone.zonelayer.LayerZone

class V1SampleGroup: SampleGroup {
    private val layers: HashMap<LayerZone, SampleLayer> = HashMap()

    override fun invokeLayer(layerZone: LayerZone): Playable {
        return layers[layerZone]?.invokeSample() ?: error("Sample group looked for a match layer in it's list, but couldn't find one.")
    }

    override fun addLayer(layerZone: LayerZone, sampleLayer: SampleLayer) {
        layers[layerZone] = sampleLayer
    }
}