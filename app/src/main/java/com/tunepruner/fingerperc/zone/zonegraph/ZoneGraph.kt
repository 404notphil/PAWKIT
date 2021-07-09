package com.tunepruner.fingerperc.zone.zonegraph

import android.graphics.PointF
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.ArticulationZone
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone

interface ZoneGraph {
    fun invokeZone(pointF: PointF): VelocityZone?
    fun addArticulationZone(articulationZone: ArticulationZone)
    fun getLayer(articulationNumber: Int, velocityNumber: Int): VelocityZone
}