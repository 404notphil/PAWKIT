package com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer

import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.BasicCoords
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.SampleCoords

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class V1SampleLayerTest {
    lateinit var roundRobinLogic: RoundRobinLogic
    lateinit var currentLayer: SampleLayer
    lateinit var basicCoords: SampleCoords

    @BeforeAll
    fun setUp() {
        roundRobinLogic = SimpleRoundRobinLogic()
        currentLayer = V1SampleLayer(1, roundRobinLogic)
        basicCoords = BasicCoords(1, 1, 1, 4, 4)
//        currentLayer.addPlayable(
//            basicCoords,
//            V1Sample(basicCoords,
//                "sdfjkl", layerLogic)
//        )
        currentLayer.addSampleCoords(1, basicCoords)
    }

    @Test
    fun invokeSampleReturnsNotNull() {
        assertNotNull(currentLayer.invokeSample())
    }

    @Test
    fun addSample() {

    }

    @Test
    fun getLayerNumber() {
    }
}