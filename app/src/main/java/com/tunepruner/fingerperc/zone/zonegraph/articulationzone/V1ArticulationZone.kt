package com.tunepruner.fingerperc.zone.zonegraph.articulationzone

import android.graphics.PointF
import com.tunepruner.fingerperc.instrument.ScreenDimensions
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.ZoneLimits
import java.util.*

class V1ArticulationZone(
    val zoneCount: Int,
    val zoneIteration: Int,
    val screenDimensions: ScreenDimensions
) : ArticulationZone {
    private val velocityZones: LinkedList<VelocityZone> = LinkedList<VelocityZone>()
    private var zoneLimits: ZoneLimits

    init {
        zoneLimits = calculateLimits(zoneCount, zoneIteration)
    }

    private fun calculateLimits(zoneCount: Int, zoneIteration: Int): ZoneLimits {
        /* Deriving top limit of this ArticulationZone from (height of a zone) * (number of preceding ones) */
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

    override fun invokeZone(pointF: PointF): VelocityZone {
        var velocityZone: VelocityZone? = null
        for (element in velocityZones) {
            if (element.isMatch(pointF)) {
                velocityZone = element
            }
        }
        return velocityZone
            ?: error("V1ArticulationZone looked for match layer in it's implementation, but didn't find a layer that contains that point")
    }


    override fun addLayer(triggerVelocityZone: VelocityZone) {
        velocityZones.add(triggerVelocityZone)
    }

    override fun getLayer(velocityNumber: Int): VelocityZone {
        return velocityZones[velocityNumber-1]
    }

    override fun getArticulationNumber(): Int{
        return zoneIteration
    }

    override fun getLimits(): ZoneLimits{
        return zoneLimits
    }
}