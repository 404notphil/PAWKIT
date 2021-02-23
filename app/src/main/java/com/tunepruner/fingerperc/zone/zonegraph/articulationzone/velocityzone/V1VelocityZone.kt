package com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone

import android.graphics.PointF
import android.util.Log
import com.tunepruner.fingerperc.instrument.ScreenDimensions

class V1VelocityZone(
    private val zoneCount: Int,
    private val zoneIteration: Int,
    private val layerIteration: Int,
    private val layerCountOfZone: Int,
    val screenDimensions: ScreenDimensions
) : VelocityZone {
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

    override fun getVelocityNumber(): Int {
        return layerIteration
    }

    override fun getLimits(): ZoneLimits {
        return zoneLimits
    }

    private fun calculateLimits(): ZoneLimits{
        /* Deriving top limit of this ArticulationZone from (height of a zone) * (number of preceding ones) */
        val thisArticulationZone = screenDimensions.screenHeight.toFloat() / zoneCount
        val articulationZoneTopLimit = thisArticulationZone * (zoneIteration - 1)

        /* Deriving top limit of this VelocityLayer from (height of a layer) * (number of preceding ones) */
        val thisLayerZoneHeight = thisArticulationZone / layerCountOfZone//TODO I don't yet account for remainders of the division, which might be causing crashes!
        Log.i("V1VelocityZone", "thisLayerZoneHeight = $thisLayerZoneHeight")
        val topLimit = articulationZoneTopLimit + thisLayerZoneHeight * (layerIteration - 1)

        val bottomLimit = topLimit + thisLayerZoneHeight

        val leftLimit = 0
        val rightLimit = screenDimensions.screenWidth
        return ZoneLimits(leftLimit, rightLimit, topLimit.toInt(), bottomLimit.toInt())

    }
}

data class ZoneLimits(val leftLimit: Int, val rightLimit: Int, val topLimit: Int, val bottomLimit: Int)