package com.tunepruner.fingerperc.instrument.sample.samplelibrary.articulation.velocitylayer.sample

class BasicCoords(
    private val groupNumber: Int,
    private val layerNumber: Int,
    private val roundRobinNumber: Int,
    private val layerCount: Int,
    private val roundRobinCount: Int
) : SampleCoords {
    override fun getGroupNumber(): Int {
        return groupNumber
    }

    override fun getLayerNumber(): Int {
        return layerNumber
    }

    override fun getRoundRobinNumber(): Int {
        return roundRobinNumber
    }

    override fun getLayerCount(): Int{
        return layerCount
    }

    override fun getRoundRobinCount(): Int{
        return roundRobinCount
    }

    override fun isSame(sampleCoords: SampleCoords): Boolean {
        return sampleCoords.getLayerNumber() == layerNumber &&
                sampleCoords.getRoundRobinNumber() == roundRobinNumber
    }


}