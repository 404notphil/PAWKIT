package com.tunepruner.fingerperc.instrument.sample

import com.tunepruner.fingerperc.instrument.sample.samplelibrary.articulation.velocitylayer.sample.Sample
import com.tunepruner.fingerperc.instrument.zone.zonegraph.articulationzone.velocityzone.VelocityZone

interface SampleManager {
    fun computeSample(velocityZone: VelocityZone): Sample
}