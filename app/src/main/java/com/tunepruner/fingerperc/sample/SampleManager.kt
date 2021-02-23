package com.tunepruner.fingerperc.sample

import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.Sample
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone

interface SampleManager {
    fun computeSample(velocityZone: VelocityZone): Sample
}