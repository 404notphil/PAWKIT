package com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer

import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.BasicCoords
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.SampleCoords
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SimpleRoundRobinLogicTest {
    lateinit var roundRobinLogic: RoundRobinLogic
    lateinit var incomingLayer: VelocityLayer
    lateinit var basicCoords: SampleCoords

    @BeforeAll
    fun setUp(){
        roundRobinLogic = SimpleRoundRobinLogic()
        incomingLayer = V1VelocityLayer(1, roundRobinLogic)
        basicCoords = BasicCoords(1, 1, 1, 4, 4)
//        incomingLayer.addPlayable(
//            basicCoords,
//            V1Sample(basicCoords,
//                "sdfjkl", layerLogic))
        incomingLayer.addSampleCoords(1, basicCoords)
    }


    @Test
    fun computeIDReturnsAValue() {
        var sampleCoords = roundRobinLogic.computeID(incomingLayer)
        println(sampleCoords.getLayerCount())
        assertNotNull(sampleCoords)
    }

    @Test
    fun computeIDReturnsCorrectLayerCount() {
        var sampleCoords = roundRobinLogic.computeID(incomingLayer)
        println(sampleCoords.getLayerCount())
        assertEquals(sampleCoords.getRoundRobinCount(), 4)
    }


}