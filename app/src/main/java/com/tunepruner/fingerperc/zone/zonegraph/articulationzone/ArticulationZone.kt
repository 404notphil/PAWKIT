package com.tunepruner.fingerperc.zone.zonegraph.articulationzone

import android.graphics.PointF
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.ZoneLimits

interface ArticulationZone {
    fun isMatch(pointF: PointF): Int
    fun invokeZone(pointF: PointF): VelocityZone?
    fun addLayer(triggerVelocityZone: VelocityZone)
    fun getLayer(velocityNumber: Int): VelocityZone
    fun getLimits(): ZoneLimits
    fun getArticulationNumber(): Int
}