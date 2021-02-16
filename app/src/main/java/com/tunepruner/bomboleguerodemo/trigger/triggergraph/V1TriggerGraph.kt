package com.tunepruner.bomboleguerodemo.trigger.triggergraph

import android.graphics.PointF
import com.tunepruner.bomboleguerodemo.trigger.triggergraph.triggerzone.TriggerZone
import com.tunepruner.bomboleguerodemo.trigger.triggergraph.triggerzone.zonelayer.LayerZone
import com.tunepruner.bomboleguerodemo.trigger.triggergraph.triggerzone.zonelayer.ZoneLimits
import java.util.LinkedList;

class V1TriggerGraph : TriggerGraph {
    private val triggerTriggerZones: LinkedList<TriggerZone> = LinkedList<TriggerZone>()

    override fun invokeLayer(pointF: PointF): LayerZone {

        var triggerTriggerZone: TriggerZone? = null
        for (element in triggerTriggerZones) {
            if (element.isMatch(pointF)) {
                triggerTriggerZone = element
            }
        }
        triggerTriggerZone?: error("TriggerManager called triggerGraph.invokeLayer(point) but got back a null value")
        return triggerTriggerZone.invokeLayer(pointF)?: error("TriggerGraph called triggerZone.invokeLayer(point) but got back a null value")
    }

    override fun getLayer(triggerZone: Int, zoneLayer: Int): LayerZone {
        return triggerTriggerZones[triggerZone-1].getLayer(zoneLayer)
    }

    override fun addTriggerZone(triggerTriggerZone: TriggerZone) {
        triggerTriggerZones.add(triggerTriggerZone)
    }

    override fun getZoneLimits(): ArrayList<ZoneLimits>{
        var zoneLimits = ArrayList<ZoneLimits>()
        for (i in 0 until triggerTriggerZones.size) {
            zoneLimits.add(triggerTriggerZones[i].getLimits())
        }
        return zoneLimits
    }
}