package com.tunepruner.fingerperc.sample.samplelibrary.articulation

import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.VelocityLayer
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.Sample
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone

class V1Articulation: Articulation {
    private val layers: HashMap<VelocityZone, VelocityLayer> = HashMap()

    override fun computeSample(velocityZone: VelocityZone): Sample {
        return layers[velocityZone]?.computeSample() ?: error("Sample group looked for a match layer in it's list, but couldn't find one.")
    }

    override fun addLayer(velocityZone: VelocityZone, velocityLayer: VelocityLayer) {
        layers[velocityZone] = velocityLayer
    }
}