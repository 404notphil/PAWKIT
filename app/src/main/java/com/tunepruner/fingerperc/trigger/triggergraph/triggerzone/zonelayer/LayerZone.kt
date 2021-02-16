package com.tunepruner.fingerperc.trigger.triggergraph.triggerzone.zonelayer

import android.graphics.PointF

interface LayerZone{
    fun isMatch(pointF: PointF): Boolean
    abstract fun getZoneIteration(): Int

    fun getLayerIteration(): Int
    fun getLimits(): ZoneLimits
}