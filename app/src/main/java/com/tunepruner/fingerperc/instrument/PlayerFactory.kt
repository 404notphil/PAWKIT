package com.tunepruner.fingerperc.instrument

import com.tunepruner.fingerperc.graphics.GUIManager
import com.tunepruner.fingerperc.sample.SampleManager
import com.tunepruner.fingerperc.trigger.TriggerManager

class PlayerFactory {
    companion object {
        fun getInstance(touchLogic: TouchLogic, triggerManager: TriggerManager, sampleManager: SampleManager, GUIManager: GUIManager, resourceManager: ResourceManager):Player{
            return SimplePlayer(touchLogic, triggerManager, sampleManager, GUIManager, resourceManager)
        }
    }
}