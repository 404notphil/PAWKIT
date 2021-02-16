package com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer

import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.BasicCoords
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.SampleCoords
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SimpleRoundRobinLogicTest {
    lateinit var roundRobinLogic: RoundRobinLogic
    lateinit var incomingLayer: SampleLayer
    lateinit var basicCoords: SampleCoords

    @BeforeAll
    fun setUp(){
        roundRobinLogic = SimpleRoundRobinLogic()
        incomingLayer = V1SampleLayer(1, roundRobinLogic)
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