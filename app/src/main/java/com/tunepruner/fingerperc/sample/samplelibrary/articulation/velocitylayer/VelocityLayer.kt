package com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer

import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.Sample
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.SampleCoords

interface VelocityLayer {
    fun computeSample(): Sample
    fun addSample(sampleCoords: SampleCoords, sample: Sample)
    fun addSampleCoords(int: Int, sampleCoords: SampleCoords)
    fun getLayerNumber(): Int
    fun getSampleIDByInt(key: Int): SampleCoords
    fun getSampleBySampleCoords(key: SampleCoords): Sample
}