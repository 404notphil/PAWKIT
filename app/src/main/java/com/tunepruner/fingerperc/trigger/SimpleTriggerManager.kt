package com.tunepruner.fingerperc.trigger

import android.graphics.PointF
import com.tunepruner.fingerperc.trigger.triggergraph.TriggerGraph
import com.tunepruner.fingerperc.trigger.triggergraph.triggerzone.zonelayer.LayerZone

class SimpleTriggerManager(private val triggerGraph: TriggerGraph): TriggerManager {
    override fun computeZoneLayer(pointF: PointF): LayerZone {
        return triggerGraph.invokeLayer(pointF)?: error("TriggerManager called triggerGraph.invokeLayer(point) but got back a null value")
    }
}