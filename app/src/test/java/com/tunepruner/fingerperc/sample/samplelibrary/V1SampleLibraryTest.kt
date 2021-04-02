package com.tunepruner.fingerperc.sample.samplelibrary

import com.tunepruner.fingerperc.instrument.ScreenDimensions
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.Articulation
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.V1Articulation
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.RoundRobinLogic
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.VelocityLayer
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.SimpleRoundRobinLogic
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.V1VelocityLayer
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.BasicCoords
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.SampleCoords
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.V1VelocityZone
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.TestInstance

import org.junit.jupiter.api.BeforeAll

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class V1SampleLibraryTest {
    lateinit var screenDimensions: ScreenDimensions
    lateinit var sampleLibrary: SampleLibrary
    lateinit var groupToAdd: Articulation
    lateinit var velocityZoneToQuery: VelocityZone
    lateinit var layerToAdd: VelocityLayer
    lateinit var basicCoords: SampleCoords
    lateinit var roundRobinLogic: RoundRobinLogic
    @BeforeAll
    fun setUp() {
        sampleLibrary = V1SampleLibrary()
        groupToAdd = V1Articulation()
        screenDimensions = ScreenDimensions(1000, 500)
        velocityZoneToQuery = V1VelocityZone(2, 1, 1, 6, 12, screenDimensions)
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
        groupToAdd.addLayer(velocityZoneToQuery, layerToAdd)
        sampleLibrary.addSampleGroup(groupToAdd)
    }

    @Test
    fun computeSampleReturnsNotNull() {
        assertNotNull(sampleLibrary.computeSample(velocityZoneToQuery))
    }

    @Test
    fun addSampleGroup() {
    }
}