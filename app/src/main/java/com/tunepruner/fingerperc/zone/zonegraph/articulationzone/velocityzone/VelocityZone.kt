package com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone

import android.graphics.PointF

interface VelocityZone{
    fun isMatch(pointF: PointF): Int
    fun getArticulationNumber(): Int
    fun getVelocityNumber(): Int
    fun getVelocityCount(): Int

    fun getLimits(): ZoneLimits
}