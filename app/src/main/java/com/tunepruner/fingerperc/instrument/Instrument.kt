package com.tunepruner.fingerperc.instrument

import android.app.Activity
import android.view.MotionEvent
import com.tunepruner.fingerperc.graphics.GUIManager
import com.tunepruner.fingerperc.graphics.SimpleGUIManager
import com.tunepruner.fingerperc.sample.SampleLibraryFactory
import com.tunepruner.fingerperc.sample.SampleManager
import com.tunepruner.fingerperc.sample.SimpleSampleManager
import com.tunepruner.fingerperc.sample.samplelibrary.SampleLibrary
import com.tunepruner.fingerperc.trigger.SimpleTriggerManager
import com.tunepruner.fingerperc.trigger.TriggerGraphFactory
import com.tunepruner.fingerperc.trigger.TriggerManager
import com.tunepruner.fingerperc.trigger.triggergraph.TriggerGraph

class Instrument(private val activity: Activity, libraryName: String) {
    private var player: Player
    private var resourceManager = ResourceManager(activity, libraryName)


    init {
        System.loadLibrary("bomboleguero")//TODO this is the JNI one, and shouldn't use the libraryName string, but should be refactored eventually!
        val touchLogic: TouchLogic = SimpleTouchLogic()
        val triggerGraph: TriggerGraph =
            TriggerGraphFactory
                .prepareTriggers(
                    ScreenPrep.getDimensions(activity), resourceManager
                )
        val sampleLibrary: SampleLibrary = SampleLibraryFactory.prepareSamples(
            triggerGraph,
            resourceManager
        )

        val triggerManager: TriggerManager = SimpleTriggerManager(triggerGraph)
        val sampleManager: SampleManager = SimpleSampleManager(sampleLibrary)
        val guiManager: GUIManager = SimpleGUIManager(resourceManager)

        player = PlayerFactory.getInstance(touchLogic, triggerManager, sampleManager, guiManager, resourceManager)

    }

    fun onTouch(event: MotionEvent) {
        player.play(event)

    }
}

