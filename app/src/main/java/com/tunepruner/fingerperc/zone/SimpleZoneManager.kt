package com.tunepruner.fingerperc.zone

import android.graphics.PointF
import com.tunepruner.fingerperc.zone.zonegraph.ZoneGraph
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone

class SimpleZoneManager(private val zoneGraph: ZoneGraph): ZoneManager {
    override fun computeVelocityLayer(pointF: PointF): VelocityZone {
        return zoneGraph.invokeZone(pointF)?: error("ZoneManager called zoneGraph.invokeLayer(point) but got back a null value")
    }
}