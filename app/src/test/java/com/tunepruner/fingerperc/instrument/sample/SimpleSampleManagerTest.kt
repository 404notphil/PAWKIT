package com.tunepruner.fingerperc.instrument.sample

import com.tunepruner.fingerperc.instrument.ScreenDimensions
import com.tunepruner.fingerperc.instrument.sample.samplelibrary.SampleLibrary
import com.tunepruner.fingerperc.instrument.sample.samplelibrary.V1SampleLibrary
import com.tunepruner.fingerperc.instrument.sample.samplelibrary.articulation.Articulation
import com.tunepruner.fingerperc.instrument.sample.samplelibrary.articulation.V1Articulation
import com.tunepruner.fingerperc.instrument.sample.samplelibrary.articulation.velocitylayer.RoundRobinLogic
import com.tunepruner.fingerperc.instrument.sample.samplelibrary.articulation.velocitylayer.VelocityLayer
import com.tunepruner.fingerperc.instrument.sample.samplelibrary.articulation.velocitylayer.SimpleRoundRobinLogic
import com.tunepruner.fingerperc.instrument.sample.samplelibrary.articulation.velocitylayer.V1VelocityLayer
import com.tunepruner.fingerperc.instrument.sample.samplelibrary.articulation.velocitylayer.sample.BasicCoords
import com.tunepruner.fingerperc.instrument.sample.samplelibrary.articulation.velocitylayer.sample.SampleCoords
import com.tunepruner.fingerperc.instrument.zone.zonegraph.articulationzone.velocityzone.V1VelocityZone
import com.tunepruner.fingerperc.instrument.zone.zonegraph.articulationzone.velocityzone.VelocityZone
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SimpleSampleManagerTest {
    lateinit var sampleManager: SimpleSampleManager
    lateinit var screenDimensions: ScreenDimensions
    lateinit var sampleLibrary: SampleLibrary
    lateinit var articulationToAdd: Articulation
    lateinit var velocityZoneToQuery: VelocityZone
    lateinit var velocityLayerToAdd: VelocityLayer
    lateinit var basicCoords: SampleCoords
    lateinit var roundRobinLogic: RoundRobinLogic
    @BeforeEach
    fun setUp() {
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
    }

    @Test
    fun computeSample() {
        assertNotNull(sampleManager.computeSample(velocityZoneToQuery))
    }
}