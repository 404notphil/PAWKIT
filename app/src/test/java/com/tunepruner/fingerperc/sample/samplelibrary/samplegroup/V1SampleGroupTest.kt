package com.tunepruner.fingerperc.sample.samplelibrary.samplegroup

import com.tunepruner.fingerperc.instrument.ScreenDimensions
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.RoundRobinLogic
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.SampleLayer
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.SimpleRoundRobinLogic
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.V1SampleLayer
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.BasicCoords
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.SampleCoords
import com.tunepruner.fingerperc.trigger.triggergraph.triggerzone.zonelayer.V1LayerZone
import com.tunepruner.fingerperc.trigger.triggergraph.triggerzone.zonelayer.LayerZone
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class V1SampleGroupTest {
    lateinit var screenDimensions: ScreenDimensions
    lateinit var currentGroup: SampleGroup
    lateinit var roundRobinLogic: RoundRobinLogic
    lateinit var layerToAdd: SampleLayer
    lateinit var basicCoords: SampleCoords
    lateinit var layerZone: LayerZone

    @BeforeAll
    fun setUp() {
        currentGroup = V1SampleGroup()
        screenDimensions = ScreenDimensions(100, 500)
        layerZone = V1LayerZone(2, 1, 1, 6, screenDimensions)
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
        currentGroup.addLayer(layerZone, layerToAdd)
    }

    @Test
    fun invokeLayer() {
        assertNotNull(currentGroup.invokeLayer(layerZone))
        //Currently fails because of comment in V1ZoneLayer
    }

    @Test
    fun addLayer() {
    }
}