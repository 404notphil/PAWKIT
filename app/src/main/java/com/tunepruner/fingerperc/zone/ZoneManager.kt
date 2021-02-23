package com.tunepruner.fingerperc.zone

import android.graphics.PointF
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone

interface ZoneManager {
    fun computeVelocityLayer(pointF: PointF): VelocityZone
}