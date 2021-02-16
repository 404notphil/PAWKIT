package com.tunepruner.bomboleguerodemo.trigger.triggergraph

import android.graphics.PointF
import com.tunepruner.bomboleguerodemo.trigger.triggergraph.triggerzone.TriggerZone
import com.tunepruner.bomboleguerodemo.trigger.triggergraph.triggerzone.zonelayer.LayerZone
import com.tunepruner.bomboleguerodemo.trigger.triggergraph.triggerzone.zonelayer.ZoneLimits

interface TriggerGraph {
    fun invokeLayer(pointF: PointF): LayerZone?
    fun addTriggerZone(triggerTriggerZone: TriggerZone)
    fun getLayer(triggerZone: Int, zoneLayer: Int): LayerZone
    fun getZoneLimits(): ArrayList<ZoneLimits>
}