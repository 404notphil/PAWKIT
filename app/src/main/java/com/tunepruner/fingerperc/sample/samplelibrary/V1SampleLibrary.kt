package com.tunepruner.fingerperc.sample.samplelibrary

import com.tunepruner.fingerperc.sample.samplelibrary.articulation.Articulation
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.Sample
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone
import java.util.*

class V1SampleLibrary : SampleLibrary {
    val articulations: ArrayList<Articulation> = ArrayList()

    override fun computeSample(velocityZone: VelocityZone): Sample {
        val currentGroup = articulations[velocityZone.getArticulationNumber()-1]
        return currentGroup.computeSample(velocityZone)
    }


    override fun addSampleGroup(articulation: Articulation) {
        articulations.add(articulation)
    }
}