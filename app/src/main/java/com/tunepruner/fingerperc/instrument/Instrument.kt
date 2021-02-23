package com.tunepruner.fingerperc.instrument

import android.app.Activity
import android.view.MotionEvent
import com.tunepruner.fingerperc.graphics.GUIManager
import com.tunepruner.fingerperc.graphics.SimpleGUIManager
import com.tunepruner.fingerperc.sample.SampleFactory
import com.tunepruner.fingerperc.sample.SampleManager
import com.tunepruner.fingerperc.sample.SimpleSampleManager
import com.tunepruner.fingerperc.sample.samplelibrary.SampleLibrary
import com.tunepruner.fingerperc.zone.SimpleZoneManager
import com.tunepruner.fingerperc.zone.ZoneFactory
import com.tunepruner.fingerperc.zone.ZoneManager
import com.tunepruner.fingerperc.zone.zonegraph.ZoneGraph

class Instrument(activity: Activity, libraryName: String) {
    private var player: Player
    private var resourceManager = ResourceManager(activity, libraryName)


    init {
        val touchLogic: TouchLogic = SimpleTouchLogic()
        val zoneGraph: ZoneGraph =
            ZoneFactory
                .prepareTriggers(
                    ScreenPrep.getDimensions(activity), resourceManager
                )
        val sampleLibrary: SampleLibrary = SampleFactory.prepareSamples(
            zoneGraph,
            resourceManager
        )

        val zoneManager: ZoneManager = SimpleZoneManager(zoneGraph)
        val sampleManager: SampleManager = SimpleSampleManager(sampleLibrary)
        val guiManager: GUIManager = SimpleGUIManager(resourceManager)

        player = PlayerFactory.getInstance(touchLogic, zoneManager, sampleManager, guiManager, resourceManager)

    }

    fun onTouch(event: MotionEvent) {
        player.play(event)
    }

    fun tearDownPlayer(){
        player.tearDown()
    }
}

