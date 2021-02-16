package com.tunepruner.fingerperc.trigger

import com.tunepruner.fingerperc.instrument.ScreenDimensions
import com.tunepruner.fingerperc.trigger.triggergraph.TriggerGraph
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test

internal class TriggerGraphFactoryTest {
    lateinit var triggerGraph: TriggerGraph
    lateinit var screenDimensions: ScreenDimensions

    @BeforeEach
    fun setUp() {
        screenDimensions = ScreenDimensions(1000, 500)
    }

    @Test
    fun prepareTriggers() {
//        triggerGraph = TriggerGraphFactory.prepareTriggers(screenDimensions, resourceManager)
//        assertNotNull(triggerGraph.getLayer(1, 1))
    }

}