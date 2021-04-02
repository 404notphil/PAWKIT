package com.tunepruner.fingerperc.sample.samplelibrary.articulation

import com.tunepruner.fingerperc.instrument.ScreenDimensions
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.RoundRobinLogic
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.VelocityLayer
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.SimpleRoundRobinLogic
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.V1VelocityLayer
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.BasicCoords
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.SampleCoords
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.V1VelocityZone
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class V1ArticulationTest {
    lateinit var screenDimensions: ScreenDimensions
    lateinit var currentGroup: Articulation
    lateinit var roundRobinLogic: RoundRobinLogic
    lateinit var layerToAdd: VelocityLayer
    lateinit var basicCoords: SampleCoords
    lateinit var velocityZone: VelocityZone

    @BeforeAll
    fun setUp() {
        currentGroup = V1Articulation()
        screenDimensions = ScreenDimensions(100, 500)
        velocityZone = V1VelocityZone(2, 1, 1, 6, 12, screenDimensions)
        roundRobinLogic = SimpleRoundRobinLogic()
        layerToAdd = V1VelocityLayer(1, roundRobinLogic)
        basicCoords = BasicCoords(1, 1, 1, 4, 4)
//        layerToAdd.addPlayable(
//            basicCoords,
//            V1Sample(
//                basicCoords,
//                "sdfjkl", layerLogic
//            )
//        )
        layerToAdd.addSampleCoords(1, basicCoords)
        currentGroup.addLayer(velocityZone, layerToAdd)
    }

    @Test
    fun invokeLayer() {
        assertNotNull(currentGroup.computeSample(velocityZone))
        //Currently fails because of comment in V1ZoneLayer
    }

    @Test
    fun addLayer() {
    }
}