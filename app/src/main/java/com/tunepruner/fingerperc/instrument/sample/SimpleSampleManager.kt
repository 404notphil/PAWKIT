package com.tunepruner.fingerperc.instrument.sample

import com.tunepruner.fingerperc.instrument.sample.samplelibrary.SampleLibrary
import com.tunepruner.fingerperc.instrument.sample.samplelibrary.articulation.velocitylayer.sample.Sample
import com.tunepruner.fingerperc.instrument.zone.zonegraph.articulationzone.velocityzone.VelocityZone

class SimpleSampleManager(val sampleLibrary: SampleLibrary): SampleManager{

    override fun computeSample(velocityZone: VelocityZone): Sample {
        return sampleLibrary.computeSample(velocityZone)
    }


}