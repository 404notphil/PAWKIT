package com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone

import android.graphics.PointF

interface VelocityZone{
    fun isMatch(pointF: PointF): Int
    abstract fun getZoneIteration(): Int

    fun getVelocityNumber(): Int
    fun getLimits(): ZoneLimits
}