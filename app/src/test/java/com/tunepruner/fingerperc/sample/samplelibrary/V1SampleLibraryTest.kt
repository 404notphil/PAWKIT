package com.tunepruner.fingerperc.sample.samplelibrary

import com.tunepruner.fingerperc.instrument.ScreenDimensions
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.SampleGroup
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.V1SampleGroup
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.RoundRobinLogic
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.SampleLayer
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.SimpleRoundRobinLogic
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.V1SampleLayer
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.BasicCoords
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.SampleCoords
import com.tunepruner.fingerperc.trigger.triggergraph.triggerzone.zonelayer.V1LayerZone
import com.tunepruner.fingerperc.trigger.triggergraph.triggerzone.zonelayer.LayerZone
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.TestInstance

import org.junit.jupiter.api.BeforeAll

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class V1SampleLibraryTest {
    lateinit var screenDimensions: ScreenDimensions
    lateinit var sampleLibrary: SampleLibrary
    lateinit var groupToAdd: SampleGroup
    lateinit var layerZoneToQuery: LayerZone
    lateinit var layerToAdd: SampleLayer
    lateinit var basicCoords: SampleCoords
    lateinit var roundRobinLogic: RoundRobinLogic
    @BeforeAll
    fun setUp() {
        sampleLibrary = V1SampleLibrary()
        groupToAdd = V1SampleGroup()
        screenDimensions = ScreenDimensions(1000, 500)
        layerZoneToQuery = V1LayerZone(2, 1, 1, 6, screenDimensions)
        roundRobinLogic = SimpleRoundRobinLogic()
        layerToAdd = V1SampleLayer(1, roundRobinLogic)
        basicCoords = BasicCoords(1, 1, 1, 4, 4)
//        layerToAdd.addPlayable(
//            basicCoords,
//            V1Sample(
//                basicCoords,
//                "sdfjkl", layerLogic
//            )
//        )
        layerToAdd.addSampleCoords(1, basicCoords)
        groupToAdd.addLayer(layerZoneToQuery, layerToAdd)
        sampleLibrary.addSampleGroup(groupToAdd)
    }

    @Test
    fun computeSampleReturnsNotNull() {
        assertNotNull(sampleLibrary.computeSample(layerZoneToQuery))
    }

    @Test
    fun addSampleGroup() {
    }
}