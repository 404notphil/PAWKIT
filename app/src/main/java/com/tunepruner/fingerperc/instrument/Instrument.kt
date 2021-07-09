package com.tunepruner.fingerperc.instrument

import android.app.Activity
import android.view.MotionEvent
import com.tunepruner.fingerperc.gui.AnimationManager
import com.tunepruner.fingerperc.gui.InstrumentGUI
import com.tunepruner.fingerperc.gui.SimpleAnimationManager
import com.tunepruner.fingerperc.instrument.sample.SampleFactory
import com.tunepruner.fingerperc.instrument.sample.SampleManager
import com.tunepruner.fingerperc.instrument.sample.SimpleSampleManager
import com.tunepruner.fingerperc.instrument.sample.samplelibrary.SampleLibrary
import com.tunepruner.fingerperc.instrument.zone.SimpleZoneManager
import com.tunepruner.fingerperc.instrument.zone.ZoneFactory
import com.tunepruner.fingerperc.instrument.zone.ZoneManager
import com.tunepruner.fingerperc.instrument.zone.zonegraph.ZoneGraph

class Instrument(activity: Activity, libraryName: String, instrumentGUI: InstrumentGUI) {
    private var player: Player
    private var resourceManager = ResourceManager(activity, libraryName)

    init {
        instrumentGUI.setupImages(activity)
        instrumentGUI.setUpTitles(activity)
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
        val animationManager: AnimationManager = SimpleAnimationManager(resourceManager, instrumentGUI)

        player = PlayerFactory.getInstance(touchLogic, zoneManager, sampleManager, animationManager, resourceManager)
    }

    fun onTouch(event: MotionEvent, activity: Activity) {
        player.play(event, activity)
    }

    fun tearDownPlayer(){
        player.tearDown()
        1 boo 2
    }

    private infix fun Int.boo(x: Int) {println(x + this)}
}

