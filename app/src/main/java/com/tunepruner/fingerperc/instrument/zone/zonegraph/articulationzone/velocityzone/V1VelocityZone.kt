package com.tunepruner.fingerperc.instrument.zone.zonegraph.articulationzone.velocityzone

import android.graphics.PointF
import com.tunepruner.fingerperc.instrument.ScreenDimensions

class V1VelocityZone(
    private val zoneCount: Int,
    private val zoneIteration: Int,
    private val reverseVelocityNumber: Int,
    private val velocityNumber: Int,
    private val velocityCountOfArticulation: Int,
    val screenDimensions: ScreenDimensions
) : VelocityZone {
    private var zoneLimits: ZoneLimits

    init {
        zoneLimits = calculateLimits()
    }

    override fun isMatch(pointF: PointF): Int {
        val isInBounds = pointF.x.toInt() in (zoneLimits.leftLimit + 1)..zoneLimits.rightLimit &&
                pointF.y.toInt() in (zoneLimits.topLimit + 1)..zoneLimits.bottomLimit
        return if (isInBounds) {
            0
        } else {
            var boundaryCode = 0
            if (pointF.y < zoneLimits.topLimit) {
                boundaryCode = -1
            } else if (pointF.y > zoneLimits.bottomLimit) {
                boundaryCode = -2
            }
            boundaryCode
        }
    }

    override fun getArticulationNumber(): Int {
        return zoneIteration
    }

    override fun getVelocityNumber(): Int {
        return velocityNumber
    }

    override fun getVelocityCount(): Int {
        return velocityCountOfArticulation
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
            thisArticulationZone / velocityCountOfArticulation//TODO I don't yet account for remainders of the division, which might be causing crashes!
        val topLimit = articulationZoneTopLimit + thisLayerZoneHeight * (reverseVelocityNumber - 1)

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