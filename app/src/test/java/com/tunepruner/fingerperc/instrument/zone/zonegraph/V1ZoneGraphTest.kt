package com.tunepruner.fingerperc.instrument.zone.zonegraph

import android.graphics.PointF
import com.tunepruner.fingerperc.instrument.ScreenDimensions
import com.tunepruner.fingerperc.instrument.zone.zonegraph.articulationzone.V1ArticulationZone
import com.tunepruner.fingerperc.instrument.zone.zonegraph.articulationzone.velocityzone.V1VelocityZone
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class V1ZoneGraphTest {
    lateinit var zoneGraph: ZoneGraph
    lateinit var triggerZone1ToAdd: V1ArticulationZone
    lateinit var triggerZone2ToAdd: V1ArticulationZone
    lateinit var point: PointF
    lateinit var layerToAdd: V1VelocityZone
    lateinit var screenDimensions: ScreenDimensions
    @BeforeEach
    fun setUp() {
        screenDimensions = ScreenDimensions(1000, 500)
        zoneGraph = V1ZoneGraph()
        triggerZone1ToAdd = V1ArticulationZone(2, 1, screenDimensions)
        triggerZone2ToAdd = V1ArticulationZone(2, 2, screenDimensions)
        zoneGraph.addArticulationZone(triggerZone1ToAdd)
        zoneGraph.addArticulationZone(triggerZone2ToAdd)
        layerToAdd = V1VelocityZone(2, 1, 1, 2, 12, screenDimensions)
        triggerZone1ToAdd.addLayer(layerToAdd)
        triggerZone2ToAdd.addLayer(layerToAdd)
        point = PointF()
        point.x = 100F
        point.y = 100F
    }

    @Test
    fun invokeLayer() {
        assertNotNull(zoneGraph.invokeZone(point))
    }
}