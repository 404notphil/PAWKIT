package com.tunepruner.fingerperc.instrument.zone.zonegraph

import android.graphics.PointF
import com.tunepruner.fingerperc.instrument.zone.zonegraph.articulationzone.ArticulationZone
import com.tunepruner.fingerperc.instrument.zone.zonegraph.articulationzone.velocityzone.VelocityZone

class V1ZoneGraph : ZoneGraph {
    private val articulationZones: ArrayList<ArticulationZone> = ArrayList()
    private val TAG =  "V1ZoneGraph"

    override fun invokeZone(pointF: PointF): VelocityZone {
        var articulationZone: ArticulationZone? = null
        loop@ for (element in articulationZones) {
            val result = element.isMatch(pointF)
            when (result) {
                0 -> {
                    articulationZone = element
//                    Log.i(TAG, result.toString())
                    break@loop
                }
                -1 -> {
                    articulationZone = articulationZones[0]
//                    Log.i(TAG, result.toString())
                }
                -2 -> {
                    articulationZone = articulationZones[articulationZones.lastIndex]
//                    Log.i(TAG, result.toString())
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
//        articulationZone
//            ?: error("ZoneManager called zoneGraph.invokeLayer(point) but got back a null value")
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