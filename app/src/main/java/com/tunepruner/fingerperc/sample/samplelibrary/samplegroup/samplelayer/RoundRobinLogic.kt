package com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer

import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.Playable
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.SampleCoords

interface RoundRobinLogic{

    fun computeID(incomingLayer: SampleLayer): SampleCoords

    fun addToHistory(playable: Playable)
}