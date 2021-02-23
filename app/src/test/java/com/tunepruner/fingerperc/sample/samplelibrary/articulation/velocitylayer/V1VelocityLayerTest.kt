package com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer

import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.BasicCoords
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.SampleCoords

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class V1VelocityLayerTest {
    lateinit var roundRobinLogic: RoundRobinLogic
    lateinit var currentLayer: VelocityLayer
    lateinit var basicCoords: SampleCoords

    @BeforeAll
    fun setUp() {
        roundRobinLogic = SimpleRoundRobinLogic()
        currentLayer = V1VelocityLayer(1, roundRobinLogic)
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
        assertNotNull(currentLayer.computeSample())
    }

    @Test
    fun addSample() {

    }

    @Test
    fun getLayerNumber() {
    }
}