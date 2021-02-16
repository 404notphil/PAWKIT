package com.tunepruner.bomboleguerodemo.trigger.triggergraph.triggerzone

import android.graphics.PointF
import com.tunepruner.bomboleguerodemo.instrument.ScreenDimensions
import com.tunepruner.bomboleguerodemo.trigger.triggergraph.triggerzone.zonelayer.LayerZone
import com.tunepruner.bomboleguerodemo.trigger.triggergraph.triggerzone.zonelayer.ZoneLimits
import java.util.*

class V1TriggerZone(
    val zoneCount: Int,
    val zoneIteration: Int,
    val screenDimensions: ScreenDimensions
) : TriggerZone {
    private val layerZones: LinkedList<LayerZone> = LinkedList<LayerZone>()
    private var zoneLimits: ZoneLimits

    init {
        zoneLimits = calculateLimits(zoneCount, zoneIteration)
    }

    private fun calculateLimits(zoneCount: Int, zoneIteration: Int): ZoneLimits {
        /* Deriving top limit of this TriggerZone from (height of a zone) * (number of preceding ones) */
        val thisZoneHeight = screenDimensions.screenHeight / zoneCount
        val topLimit = thisZoneHeight * (zoneIteration - 1)
        val bottomLimit = topLimit + thisZoneHeight

        val leftLimit = 0
        val rightLimit = screenDimensions.screenWidth
        return ZoneLimits(leftLimit, rightLimit, topLimit, bottomLimit)
    }

    override fun isMatch(pointF: PointF): Boolean {
        return pointF.x.toInt() in (zoneLimits.leftLimit + 1)..zoneLimits.rightLimit &&
                pointF.y.toInt() in (zoneLimits.topLimit + 1)..zoneLimits.bottomLimit
    }

    override fun invokeLayer(pointF: PointF): LayerZone {
        var layerZone: LayerZone? = null
        for (element in layerZones) {
            if (element.isMatch(pointF)) {
                layerZone = element
            }
        }
        return layerZone
            ?: error("V1TriggerZone looked for match layer in it's implementation, but didn't find a layer that contains that point")
    }


    override fun addLayer(triggerLayerZone: LayerZone) {
        layerZones.add(triggerLayerZone)
    }

    override fun getLayer(zoneLayer: Int): LayerZone {
        return layerZones[zoneLayer-1]
    }

    override fun getZoneNumber(): Int{
        return zoneIteration
    }

    override fun getLimits(): ZoneLimits{
        return zoneLimits
    }
}