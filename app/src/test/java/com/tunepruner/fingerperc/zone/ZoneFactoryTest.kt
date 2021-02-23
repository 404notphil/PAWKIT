package com.tunepruner.fingerperc.zone

import com.tunepruner.fingerperc.instrument.ScreenDimensions
import com.tunepruner.fingerperc.zone.zonegraph.ZoneGraph
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test

internal class ZoneFactoryTest {
    lateinit var zoneGraph: ZoneGraph
    lateinit var screenDimensions: ScreenDimensions

    @BeforeEach
    fun setUp() {
        screenDimensions = ScreenDimensions(1000, 500)
    }

    @Test
    fun prepareTriggers() {
//        zoneGraph = ZoneFactory.prepareTriggers(screenDimensions, resourceManager)
//        assertNotNull(zoneGraph.getLayer(1, 1))
    }

}