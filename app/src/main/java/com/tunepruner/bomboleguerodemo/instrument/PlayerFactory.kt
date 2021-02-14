package com.tunepruner.bomboleguerodemo.instrument

import com.tunepruner.bomboleguerodemo.sample.SampleManager
import com.tunepruner.bomboleguerodemo.trigger.TriggerManager

class PlayerFactory {
    companion object {
        fun getInstance(touchLogic: TouchLogic, triggerManager: TriggerManager, sampleManager: SampleManager, resourceManager: ResourceManager):Player{
            return SimplePlayer(touchLogic, triggerManager, sampleManager, resourceManager)
        }
    }
}