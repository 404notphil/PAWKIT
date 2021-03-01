package com.tunepruner.fingerperc.zone.zonegraph.articulationzone

import android.graphics.PointF
import android.util.Log
import com.tunepruner.fingerperc.instrument.ScreenDimensions
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.ZoneLimits
import java.util.*

class V1ArticulationZone(
    val zoneCount: Int,
    val zoneIteration: Int,
    val screenDimensions: ScreenDimensions
) : ArticulationZone {
    private val TAG = "ArticulationZone"
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

    override fun isMatch(pointF: PointF): Int {
        val isInBounds = pointF.x.toInt() in (zoneLimits.leftLimit + 1)..zoneLimits.rightLimit &&
                pointF.y.toInt() in (zoneLimits.topLimit + 1)..zoneLimits.bottomLimit
        return if (isInBounds) {
            0
        } else{
            var boundaryCode = 0
            if (pointF.y < zoneLimits.topLimit){
                boundaryCode = -1
            } else if (pointF.y > zoneLimits.bottomLimit){
                boundaryCode = -2
            }
            boundaryCode
        }
    }

    override fun invokeZone(pointF: PointF): VelocityZone {
        var velocityZone: VelocityZone? = null
//        for (element in velocityZones) {
//            if (element.compareWithLimits(pointF)) {
//                velocityZone = element
//            }
//        }

        for (element in velocityZones) {
            when (val result = element.isMatch(pointF)){
                0 -> {
                    velocityZone = element
                    Log.i(TAG, result.toString())
                    return velocityZone
                }
                -1 -> {
                    velocityZone = velocityZones.first
                    Log.i(TAG, result.toString())
                }
                -2 -> {
                    velocityZone = velocityZones[velocityZones.lastIndex]
                    Log.i(TAG, result.toString())
                }
            }
        }
        if (velocityZone == null){
            velocityZone = if(pointF.y <= 0){
                velocityZones[0]
            }else{
                velocityZones[velocityZones.lastIndex]
            }
        }

        return velocityZone
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