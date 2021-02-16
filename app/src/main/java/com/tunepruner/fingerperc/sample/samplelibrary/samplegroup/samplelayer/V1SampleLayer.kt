package com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer

import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.Playable
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.SampleCoords

class V1SampleLayer(private val layerNumber: Int, private val roundRobinLogic: RoundRobinLogic) :
    SampleLayer {
    private val playablesBySampleCoords: HashMap<SampleCoords, Playable> = HashMap()
    private val sampleCoordsByInt: HashMap<Int, SampleCoords> = HashMap()

    override fun invokeSample(): Playable {
        val sampleCoords = roundRobinLogic.computeID(this)
        return playablesBySampleCoords[sampleCoords] ?: error("Sample not found in layer")
    }

    override fun addPlayable(sampleCoords: SampleCoords, playable: Playable) {
        playablesBySampleCoords[sampleCoords] = playable
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

    override fun getPlayableBySampleCoords(key: SampleCoords): Playable {
        return playablesBySampleCoords[key] ?:
        return playablesBySampleCoords[1] ?:
        error("playablesBySampleCoords not found")
    }
}