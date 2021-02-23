package com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample

interface SampleCoords {
    fun getGroupNumber(): Int
    fun getLayerNumber(): Int
    fun getRoundRobinNumber(): Int
    fun isSame(sampleCoords: SampleCoords): Boolean
    fun getRoundRobinCount(): Int
    fun getLayerCount(): Int
}