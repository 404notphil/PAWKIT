package com.tunepruner.fingerperc.sample

import com.tunepruner.fingerperc.sample.samplelibrary.SampleLibrary
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.Playable
import com.tunepruner.fingerperc.trigger.triggergraph.triggerzone.zonelayer.LayerZone

class SimpleSampleManager(val sampleLibrary: SampleLibrary): SampleManager{

    override fun computeSample(layerZone: LayerZone): Playable {
        return sampleLibrary.computeSample(layerZone)
    }


}