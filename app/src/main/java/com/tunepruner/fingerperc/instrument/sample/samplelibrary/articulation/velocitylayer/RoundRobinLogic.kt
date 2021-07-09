package com.tunepruner.fingerperc.instrument.sample.samplelibrary.articulation.velocitylayer

import com.tunepruner.fingerperc.instrument.sample.samplelibrary.articulation.velocitylayer.sample.Sample
import com.tunepruner.fingerperc.instrument.sample.samplelibrary.articulation.velocitylayer.sample.SampleCoords

interface RoundRobinLogic{

    fun computeID(incomingLayer: VelocityLayer): SampleCoords

    fun addToHistory(sample: Sample)
}