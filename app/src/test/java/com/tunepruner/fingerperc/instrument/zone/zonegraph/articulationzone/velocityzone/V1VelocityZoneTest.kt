package com.tunepruner.fingerperc.instrument.zone.zonegraph.articulationzone.velocityzone

import android.graphics.PointF
import com.tunepruner.fingerperc.instrument.ScreenDimensions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class V1VelocityZoneTest {
    lateinit var screenDimensions: ScreenDimensions

    @BeforeAll
    fun setUp() {
        screenDimensions = ScreenDimensions(1000, 500)
    }

    @Test
    fun calculateLimits() {
        var zoneLayer = V1VelocityZone(2, 1, 1, 6, 12, screenDimensions)

    }

    @Test
    fun isMatch() {
        var point = PointF()
        point.x = 100F
        point.y = 166F
        var zoneLayer = V1VelocityZone(2, 1, 2, 6, 12, ScreenDimensions(1000, 500))
        assertEquals(0, zoneLayer.isMatch(point))
    }

    @Test
    fun getZoneIteration() {
    }
}