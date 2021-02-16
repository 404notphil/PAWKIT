package com.tunepruner.fingerperc.trigger

import android.graphics.PointF
import com.tunepruner.fingerperc.trigger.triggergraph.triggerzone.zonelayer.LayerZone

interface TriggerManager {
    fun computeZoneLayer(pointF: PointF): LayerZone
}