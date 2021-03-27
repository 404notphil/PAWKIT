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
    private val TAG = "V1VelocityZone"

    init {
        zoneLimits = calculateLimits()
    }

    override fun isMatch(pointF: PointF): Int {
        val isInBounds = pointF.x.toInt() in (zoneLimits.leftLimit + 1)..zoneLimits.rightLimit &&
                pointF.y.toInt() in (zoneLimits.topLimit + 1)..zoneLimits.bottomLimit
        return if (isInBounds) {
//            Log.i(TAG, "a match!")
            0
        } else {
            var boundaryCode = 0
            if (pointF.y < zoneLimits.topLimit) {
                boundaryCode = -1
//                Log.i(TAG, boundaryCode.toString())
            } else if (pointF.y > zoneLimits.bottomLimit) {
                boundaryCode = -2
//                Log.i(TAG, boundaryCode.toString())
            }
            boundaryCode
        }
    }

    override fun getArticulationNumber(): Int {
        return zoneIteration
    }

    override fun getVelocityNumber(): Int {
        return layerIteration
    }

    override fun getLimits(): ZoneLimits {
        return zoneLimits
    }

    private fun calculateLimits(): ZoneLimits {
        /* Deriving top limit of this ArticulationZone from (height of a zone) * (number of preceding ones) */
        val thisArticulationZone = screenDimensions.screenHeight.toFloat() / zoneCount
        val articulationZoneTopLimit = thisArticulationZone * (zoneIteration - 1)

        /* Deriving top limit of this VelocityLayer from (height of a layer) * (number of preceding ones) */
        val thisLayerZoneHeight =
            thisArticulationZone / layerCountOfZone//TODO I don't yet account for remainders of the division, which might be causing crashes!
//        Log.i(TAG, "thisLayerZoneHeight = $thisLayerZoneHeight")
        val topLimit = articulationZoneTopLimit + thisLayerZoneHeight * (layerIteration - 1)

        val bottomLimit = topLimit + thisLayerZoneHeight

        val leftLimit = 0
        val rightLimit = screenDimensions.screenWidth
        return ZoneLimits(leftLimit, rightLimit, topLimit.toInt(), bottomLimit.toInt())

    }
}

data class ZoneLimits(
    val leftLimit: Int,
    val rightLimit: Int,
    val topLimit: Int,
    val bottomLimit: Int
)