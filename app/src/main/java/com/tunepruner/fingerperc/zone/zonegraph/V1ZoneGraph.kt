package com.tunepruner.fingerperc.zone.zonegraph

import android.graphics.PointF
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.ArticulationZone
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone
import java.util.LinkedList;

class V1ZoneGraph : ZoneGraph {
    private val articulationZones: LinkedList<ArticulationZone> = LinkedList<ArticulationZone>()

    override fun invokeZone(pointF: PointF): VelocityZone {

        var articulationZone: ArticulationZone? = null
        for (element in articulationZones) {
            if (element.isMatch(pointF)) {
                articulationZone = element
            }
        }
        articulationZone?: error("ZoneManager called zoneGraph.invokeLayer(point) but got back a null value")
        return articulationZone.invokeZone(pointF)?: error("ZoneGraph called articulationZone.invokeLayer(point) but got back a null value")
    }

    override fun getLayer(articulationNumber: Int, velocityNumber: Int): VelocityZone {
        return articulationZones[articulationNumber-1].getLayer(velocityNumber)
    }

    override fun addArticulationZone(articulationZone: ArticulationZone) {
        articulationZones.add(articulationZone)
    }

}