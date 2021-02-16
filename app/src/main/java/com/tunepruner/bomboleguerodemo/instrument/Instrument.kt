package com.tunepruner.bomboleguerodemo.instrument

import android.app.Activity
import android.view.MotionEvent
import com.tunepruner.bomboleguerodemo.graphics.GUIManager
import com.tunepruner.bomboleguerodemo.graphics.SimpleGUIManager
import com.tunepruner.bomboleguerodemo.sample.SampleLibraryFactory
import com.tunepruner.bomboleguerodemo.sample.SampleManager
import com.tunepruner.bomboleguerodemo.sample.SimpleSampleManager
import com.tunepruner.bomboleguerodemo.sample.samplelibrary.SampleLibrary
import com.tunepruner.bomboleguerodemo.trigger.SimpleTriggerManager
import com.tunepruner.bomboleguerodemo.trigger.TriggerGraphFactory
import com.tunepruner.bomboleguerodemo.trigger.TriggerManager
import com.tunepruner.bomboleguerodemo.trigger.triggergraph.TriggerGraph

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

