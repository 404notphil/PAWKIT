package com.tunepruner.fingerperc.instrument

import android.graphics.Point
import com.tunepruner.fingerperc.sample.SimpleSampleManager
import com.tunepruner.fingerperc.sample.samplelibrary.SampleLibrary
import com.tunepruner.fingerperc.sample.samplelibrary.V1SampleLibrary
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.Articulation
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.V1Articulation
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.RoundRobinLogic
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.VelocityLayer
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.SimpleRoundRobinLogic
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.V1VelocityLayer
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.BasicCoords
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.SampleCoords
import com.tunepruner.fingerperc.zone.SimpleZoneManager
import com.tunepruner.fingerperc.zone.zonegraph.ZoneGraph
import com.tunepruner.fingerperc.zone.zonegraph.V1ZoneGraph
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.V1ArticulationZone
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.V1VelocityZone
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class OboePlayerTest {

    lateinit var player: OboePlayer
    lateinit var triggerManager: SimpleZoneManager
    lateinit var zoneGraph: ZoneGraph
    lateinit var triggerZone1ToAdd: V1ArticulationZone
    lateinit var triggerZone2ToAdd: V1ArticulationZone
    lateinit var point: Point
    lateinit var layerToAdd: V1VelocityZone
    lateinit var screenDimensions: ScreenDimensions

    lateinit var sampleManager: SimpleSampleManager
    lateinit var sampleLibrary: SampleLibrary
    lateinit var articulationToAdd: Articulation
    lateinit var velocityZoneToQuery: VelocityZone
    lateinit var velocityLayerToAdd: VelocityLayer
    lateinit var basicCoords: SampleCoords
    lateinit var roundRobinLogic: RoundRobinLogic

    @BeforeAll
    fun setUpTriggerManager() {
        screenDimensions = ScreenDimensions(1000, 500)
        zoneGraph = V1ZoneGraph()
        triggerManager = SimpleZoneManager(zoneGraph)
        triggerZone1ToAdd = V1ArticulationZone(2, 1, screenDimensions)
        triggerZone2ToAdd = V1ArticulationZone(2, 2, screenDimensions)
        zoneGraph.addArticulationZone(triggerZone1ToAdd)
        zoneGraph.addArticulationZone(triggerZone2ToAdd)
        layerToAdd = V1VelocityZone(2, 1, 1, 2, 12, screenDimensions)
        triggerZone1ToAdd.addLayer(layerToAdd)
        triggerZone2ToAdd.addLayer(layerToAdd)
        point = Point()
        point.x = 10
        point.y = 10

        sampleLibrary = V1SampleLibrary()
        articulationToAdd = V1Articulation()
        screenDimensions = ScreenDimensions(1000, 500)
        velocityZoneToQuery = V1VelocityZone(2, 1, 1, 6, 12, screenDimensions)
        roundRobinLogic = SimpleRoundRobinLogic()
        velocityLayerToAdd = V1VelocityLayer(1, roundRobinLogic)
        basicCoords = BasicCoords(1, 1, 1, 4, 4)
//        velocityLayerToAdd.addPlayable(
//            basicCoords,
//            V1Sample(
//                basicCoords,
//                "sdfjkl", layerLogic
//            )
//        )
        velocityLayerToAdd.addSampleCoords(1, basicCoords)
        articulationToAdd.addLayer(velocityZoneToQuery, velocityLayerToAdd)
        sampleLibrary.addSampleGroup(articulationToAdd)
        sampleManager = SimpleSampleManager(sampleLibrary)
//        player = OboePlayer(triggerManager, sampleManager)
    }

    @Test
    fun play() {
//        assertNotNull(player.play(point))
    }
}