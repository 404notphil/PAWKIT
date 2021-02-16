package com.tunepruner.fingerperc.trigger.triggergraph.triggerzone.zonelayer

import android.graphics.Point
import com.tunepruner.fingerperc.instrument.ScreenDimensions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class V1LayerZoneTest {
    lateinit var screenDimensions: ScreenDimensions

    @BeforeAll
    fun setUp() {
        screenDimensions = ScreenDimensions(1000, 500)
    }

    @Test
    fun calculateLimits() {
        var zoneLayer = V1LayerZone(2, 1, 1, 6, screenDimensions)

    }

    @Test
    fun isMatch() {
        var point = Point()
        point.x = 100
        point.y = 166
        var zoneLayer = V1LayerZone(2, 1, 2, 6, ScreenDimensions(1000, 500))
        assertTrue(zoneLayer.isMatch(point))
    }

    @Test
    fun getZoneIteration() {
    }
}