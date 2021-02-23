package com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer

import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.Sample
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.SampleCoords

class V1VelocityLayer(private val layerNumber: Int, private val roundRobinLogic: RoundRobinLogic) :
    VelocityLayer {
    private val samplesBySampleCoords: HashMap<SampleCoords, Sample> = HashMap()
    private val sampleCoordsByInt: HashMap<Int, SampleCoords> = HashMap()

    override fun computeSample(): Sample {
        val sampleCoords = roundRobinLogic.computeID(this)
        return samplesBySampleCoords[sampleCoords] ?: error("Sample not found in layer")
    }

    override fun addSample(sampleCoords: SampleCoords, sample: Sample) {
        samplesBySampleCoords[sampleCoords] = sample
    }

    override fun addSampleCoords(int: Int, sampleCoords: SampleCoords) {
        sampleCoordsByInt[int] = sampleCoords
    }

    override fun getLayerNumber(): Int {
        return layerNumber
    }

    override fun getSampleIDByInt(key: Int): SampleCoords {
        return sampleCoordsByInt[key] ?:
        return sampleCoordsByInt[1] ?:
        error("SampleCoords not found")
    }

    override fun getSampleBySampleCoords(key: SampleCoords): Sample {
        return samplesBySampleCoords[key] ?:
        return samplesBySampleCoords[1] ?:
        error("playablesBySampleCoords not found")
    }
}