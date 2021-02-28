package com.tunepruner.fingerperc.zone.zonegraph

import android.graphics.PointF
import android.util.Log
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.ArticulationZone
import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone
import java.util.LinkedList;

class V1ZoneGraph : ZoneGraph {
    private val articulationZones: LinkedList<ArticulationZone> = LinkedList<ArticulationZone>()
    private val TAG =  "V1ZoneGraph"

    override fun invokeZone(pointF: PointF): VelocityZone {
        var articulationZone: ArticulationZone? = null
        for (element in articulationZones) {
            val result = element.isMatch(pointF)
            if (result == 0) {
                articulationZone = element
                Log.i(TAG, result.toString())
                break
            } else if (element.isMatch(pointF) == -1) {
                articulationZone = articulationZones.first
                Log.i(TAG, result.toString())
            } else if (element.isMatch(pointF) == -2) {
                articulationZone = articulationZones[articulationZones.lastIndex]
                Log.i(TAG, result.toString())
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