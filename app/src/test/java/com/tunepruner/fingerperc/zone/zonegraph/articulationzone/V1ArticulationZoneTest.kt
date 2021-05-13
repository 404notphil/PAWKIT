package com.tunepruner.fingerperc.zone.zonegraph.articulationzone

import android.graphics.Point
import android.graphics.PointF
import com.tunepruner.fingerperc.instrument.ScreenDimensions
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.V1VelocityZone
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class V1ArticulationZoneTest {
    lateinit var triggerZone: V1ArticulationZone
    lateinit var point: PointF
    lateinit var layerToAdd: V1VelocityZone
    lateinit var screenDimensions: ScreenDimensions

    @BeforeEach
    fun setUp() {
        screenDimensions = ScreenDimensions(1000, 500)
        triggerZone = V1ArticulationZone(2, 1, screenDimensions)
        layerToAdd = V1VelocityZone(2, 1, 1, 2, 12, screenDimensions)
        triggerZone.addLayer(layerToAdd)
        point = PointF()
        point.x = 100F
        point.y = 100F
    }

    @Test
    fun invokeLayerNotNull() {
        assertNotNull(triggerZone.invokeZone(point))
    }

    @Test
    fun getZoneIterationCorrectNumber() {
        assertEquals(triggerZone.invokeZone(point).getArticulationNumber(), 1)
    }
}