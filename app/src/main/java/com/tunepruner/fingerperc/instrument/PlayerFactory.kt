package com.tunepruner.fingerperc.instrument

import com.tunepruner.fingerperc.graphics.GUIManager
import com.tunepruner.fingerperc.sample.SampleManager
import com.tunepruner.fingerperc.zone.ZoneManager

class PlayerFactory {
    companion object {
        fun getInstance(touchLogic: TouchLogic, zoneManager: ZoneManager, sampleManager: SampleManager, GUIManager: GUIManager, resourceManager: ResourceManager):Player{
            return OboePlayer(touchLogic, zoneManager, sampleManager, GUIManager, resourceManager)
        }
    }
}