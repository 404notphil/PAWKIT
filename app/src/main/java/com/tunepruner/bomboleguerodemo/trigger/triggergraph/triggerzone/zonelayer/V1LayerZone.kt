package com.tunepruner.bomboleguerodemo.trigger.triggergraph.triggerzone.zonelayer

import android.graphics.PointF
import android.util.Log
import com.tunepruner.bomboleguerodemo.instrument.ScreenDimensions

class V1LayerZone(
    private val zoneCount: Int,
    private val zoneIteration: Int,
    private val layerIteration: Int,
    private val layerCountOfZone: Int,
    val screenDimensions: ScreenDimensions
) : LayerZone {
    private var zoneLimits: ZoneLimits

    init {
        zoneLimits = calculateLimits()
    }

    override fun isMatch(pointF: PointF): Boolean {
        return pointF.x.toInt() in (zoneLimits.leftLimit + 1)..zoneLimits.rightLimit &&
                pointF.y.toInt() in (zoneLimits.topLimit + 1)..zoneLimits.bottomLimit
    }

    override fun getZoneIteration(): Int {
        return zoneIteration
    }

    override fun getLayerIteration(): Int {
        return layerIteration
    }

    override fun getLimits(): ZoneLimits {
        return zoneLimits
    }

    private fun calculateLimits(): ZoneLimits{
        /* Deriving top limit of this TriggerZone from (height of a zone) * (number of preceding ones) */
        val thisTriggerZone = screenDimensions.screenHeight.toFloat() / zoneCount
        val triggerZoneTopLimit = thisTriggerZone * (zoneIteration - 1)

        /* Deriving top limit of this ZoneLayer from (height of a layer) * (number of preceding ones) */
        val thisLayerZoneHeight = thisTriggerZone / layerCountOfZone//TODO I don't yet account for remainders of the division, which might be causing crashes!
        Log.i("V1LayerZone", "thisLayerZoneHeight = $thisLayerZoneHeight")
        val topLimit = triggerZoneTopLimit + thisLayerZoneHeight * (layerIteration - 1)

        val bottomLimit = topLimit + thisLayerZoneHeight

        val leftLimit = 0
        val rightLimit = screenDimensions.screenWidth
        return ZoneLimits(leftLimit, rightLimit, topLimit.toInt(), bottomLimit.toInt())

    }
}

data class ZoneLimits(val leftLimit: Int, val rightLimit: Int, val topLimit: Int, val bottomLimit: Int)