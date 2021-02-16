package com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer

import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.Playable
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.SampleCoords

interface SampleLayer {
    fun invokeSample(): Playable
    fun addPlayable(sampleCoords: SampleCoords, playable: Playable)
    fun addSampleCoords(int: Int, sampleCoords: SampleCoords)
    fun getLayerNumber(): Int
    fun getSampleIDByInt(key: Int): SampleCoords
    fun getPlayableBySampleCoords(key: SampleCoords): Playable
}