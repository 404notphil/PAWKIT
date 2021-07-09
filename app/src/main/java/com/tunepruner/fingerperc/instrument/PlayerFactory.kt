package com.tunepruner.fingerperc.instrument

import com.tunepruner.fingerperc.gui.AnimationManager
import com.tunepruner.fingerperc.instrument.sample.SampleManager
import com.tunepruner.fingerperc.instrument.zone.ZoneManager

class PlayerFactory {
    companion object {
        fun getInstance(touchLogic: TouchLogic, zoneManager: ZoneManager, sampleManager: SampleManager, animationManager: AnimationManager, resourceManager: ResourceManager):Player{
            return OboePlayer(touchLogic, zoneManager, sampleManager, animationManager, resourceManager)
        }
    }
}