package com.tunepruner.fingerperc.zone

import android.graphics.Point
import com.tunepruner.fingerperc.instrument.ScreenDimensions
import com.tunepruner.fingerperc.zone.zonegraph.ZoneGraph
import com.tunepruner.fingerperc.zone.zonegraph.V1ZoneGraph
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.V1ArticulationZone
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.V1VelocityZone
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SimpleZoneManagerTest {

    lateinit var triggerManager: SimpleZoneManager
    lateinit var zoneGraph: ZoneGraph
    lateinit var triggerZone1ToAdd: V1ArticulationZone
    lateinit var triggerZone2ToAdd: V1ArticulationZone
    lateinit var point: Point
    lateinit var layerToAdd: V1VelocityZone
    lateinit var screenDimensions: ScreenDimensions
    @BeforeEach
    fun setUp() {
        screenDimensions = ScreenDimensions(1000, 500)
        zoneGraph = V1ZoneGraph()
        triggerManager = SimpleZoneManager(zoneGraph)
        triggerZone1ToAdd = V1ArticulationZone(2, 1, screenDimensions)
        triggerZone2ToAdd = V1ArticulationZone(2, 2, screenDimensions)
        zoneGraph.addArticulationZone(triggerZone1ToAdd)
        zoneGraph.addArticulationZone(triggerZone2ToAdd)
        layerToAdd = V1VelocityZone(2, 1, 1, 2, screenDimensions)
        triggerZone1ToAdd.addLayer(layerToAdd)
        triggerZone2ToAdd.addLayer(layerToAdd)
        point = Point()
        point.x = 100
        point.y = 100
    }

    @Test
    fun computeZoneLayerReturnsNotNull() {
        assertNotNull(triggerManager.computeZoneLayer(point))
    }
}