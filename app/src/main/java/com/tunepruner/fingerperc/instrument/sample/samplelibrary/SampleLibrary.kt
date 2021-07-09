package com.tunepruner.fingerperc.instrument.sample.samplelibrary

import com.tunepruner.fingerperc.instrument.sample.samplelibrary.articulation.Articulation
import com.tunepruner.fingerperc.instrument.sample.samplelibrary.articulation.velocitylayer.sample.Sample
import com.tunepruner.fingerperc.instrument.zone.zonegraph.articulationzone.velocityzone.VelocityZone

interface SampleLibrary {
    fun computeSample(velocityZone: VelocityZone): Sample
    fun addSampleGroup(articulation: Articulation)
}