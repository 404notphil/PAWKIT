package com.tunepruner.fingerperc.sample.samplelibrary

import com.tunepruner.fingerperc.sample.samplelibrary.articulation.Articulation
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.Sample
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone

interface SampleLibrary {
    fun computeSample(velocityZone: VelocityZone): Sample
    fun addSampleGroup(articulation: Articulation)
}