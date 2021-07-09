package com.tunepruner.fingerperc.instrument.sample.samplelibrary.articulation.velocitylayer.sample

import com.tunepruner.fingerperc.instrument.FileSnapshot


class V1Sample(
    private val sampleCoords: SampleCoords,
    private val fileSnapshot: FileSnapshot
) :
    Sample {

    override fun getSampleCoords(): SampleCoords {
        return sampleCoords
    }

    override fun getIndex(): Int {
        return fileSnapshot.index
    }

}
