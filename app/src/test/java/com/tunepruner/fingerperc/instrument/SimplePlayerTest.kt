package com.tunepruner.fingerperc.instrument

import android.graphics.Point
import com.tunepruner.fingerperc.sample.SimpleSampleManager
import com.tunepruner.fingerperc.sample.samplelibrary.SampleLibrary
import com.tunepruner.fingerperc.sample.samplelibrary.V1SampleLibrary
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.SampleGroup
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.V1SampleGroup
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.RoundRobinLogic
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.SampleLayer
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.SimpleRoundRobinLogic
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.V1SampleLayer
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.BasicCoords
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.SampleCoords
import com.tunepruner.fingerperc.trigger.SimpleTriggerManager
import com.tunepruner.fingerperc.trigger.triggergraph.TriggerGraph
import com.tunepruner.fingerperc.trigger.triggergraph.V1TriggerGraph
import com.tunepruner.fingerperc.trigger.triggergraph.triggerzone.V1TriggerZone
import com.tunepruner.fingerperc.trigger.triggergraph.triggerzone.zonelayer.V1LayerZone
import com.tunepruner.fingerperc.trigger.triggergraph.triggerzone.zonelayer.LayerZone
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SimplePlayerTest {

    lateinit var player: SimplePlayer
    lateinit var triggerManager: SimpleTriggerManager
    lateinit var triggerGraph: TriggerGraph
    lateinit var triggerZone1ToAdd: V1TriggerZone
    lateinit var triggerZone2ToAdd: V1TriggerZone
    lateinit var point: Point
    lateinit var layerToAdd: V1LayerZone
    lateinit var screenDimensions: ScreenDimensions

    lateinit var sampleManager: SimpleSampleManager
    lateinit var sampleLibrary: SampleLibrary
    lateinit var sampleGroupToAdd: SampleGroup
    lateinit var layerZoneToQuery: LayerZone
    lateinit var sampleLayerToAdd: SampleLayer
    lateinit var basicCoords: SampleCoords
    lateinit var roundRobinLogic: RoundRobinLogic

    @BeforeAll
    fun setUpTriggerManager() {
        screenDimensions = ScreenDimensions(1000, 500)
        triggerGraph = V1TriggerGraph()
        triggerManager = SimpleTriggerManager(triggerGraph)
        triggerZone1ToAdd = V1TriggerZone(2, 1, screenDimensions)
        triggerZone2ToAdd = V1TriggerZone(2, 2, screenDimensions)
        triggerGraph.addTriggerZone(triggerZone1ToAdd)
        triggerGraph.addTriggerZone(triggerZone2ToAdd)
        layerToAdd = V1LayerZone(2, 1, 1, 2, screenDimensions)
        triggerZone1ToAdd.addLayer(layerToAdd)
        triggerZone2ToAdd.addLayer(layerToAdd)
        point = Point()
        point.x = 10
        point.y = 10

        sampleLibrary = V1SampleLibrary()
        sampleGroupToAdd = V1SampleGroup()
        screenDimensions = ScreenDimensions(1000, 500)
        layerZoneToQuery = V1LayerZone(2, 1, 1, 6, screenDimensions)
        roundRobinLogic = SimpleRoundRobinLogic()
        sampleLayerToAdd = V1SampleLayer(1, roundRobinLogic)
        basicCoords = BasicCoords(1, 1, 1, 4, 4)
//        sampleLayerToAdd.addPlayable(
//            basicCoords,
//            V1Sample(
//                basicCoords,
//                "sdfjkl", layerLogic
//            )
//        )
        sampleLayerToAdd.addSampleCoords(1, basicCoords)
        sampleGroupToAdd.addLayer(layerZoneToQuery, sampleLayerToAdd)
        sampleLibrary.addSampleGroup(sampleGroupToAdd)
        sampleManager = SimpleSampleManager(sampleLibrary)
//        player = SimplePlayer(triggerManager, sampleManager)
    }

    @Test
    fun play() {
//        assertNotNull(player.play(point))
    }
}