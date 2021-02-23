package com.tunepruner.fingerperc.sample

import com.tunepruner.fingerperc.sample.samplelibrary.SampleLibrary
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.Sample
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone

class SimpleSampleManager(val sampleLibrary: SampleLibrary): SampleManager{

    override fun computeSample(velocityZone: VelocityZone): Sample {
        return sampleLibrary.computeSample(velocityZone)
    }


}