package com.tunepruner.fingerperc.instrument.zone

import android.graphics.PointF
import com.tunepruner.fingerperc.instrument.zone.zonegraph.articulationzone.velocityzone.VelocityZone

interface ZoneManager {
    fun computeVelocityLayer(pointF: PointF): VelocityZone
}