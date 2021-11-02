package com.tunepruner.fingerperc.instrument.zone.zonegraph

import android.graphics.PointF
import com.tunepruner.fingerperc.instrument.zone.zonegraph.articulationzone.ArticulationZone
import com.tunepruner.fingerperc.instrument.zone.zonegraph.articulationzone.velocityzone.VelocityZone

class V1ZoneGraph : ZoneGraph {
    private val articulationZones: ArrayList<ArticulationZone> = ArrayList()

    override fun invokeZone(pointF: PointF): VelocityZone {
        var articulationZone: ArticulationZone? = null
        loop@ for (element in articulationZones) {
            when (element.isMatch(pointF)) {
                0 -> {
                    articulationZone = element
                    break@loop
                }
                -1 -> {
                    articulationZone = articulationZones[0]
                }
                -2 -> {
                    articulationZone = articulationZones[articulationZones.lastIndex]
                }
            }
        }
        if (articulationZone == null){
            articulationZone = if(pointF.y <= 0){
                articulationZones[0]
            }else{
                articulationZones[articulationZones.lastIndex]
            }
        }
        return articulationZone.invokeZone(pointF)
            ?: error("ZoneGraph called articulationZone.invokeLayer(point) but got back a null value")
    }

    override fun getLayer(articulationNumber: Int, velocityNumber: Int): VelocityZone {
        return articulationZones[articulationNumber - 1].getLayer(velocityNumber)
    }

    override fun addArticulationZone(articulationZone: ArticulationZone) {
        articulationZones.add(articulationZone)
    }

}