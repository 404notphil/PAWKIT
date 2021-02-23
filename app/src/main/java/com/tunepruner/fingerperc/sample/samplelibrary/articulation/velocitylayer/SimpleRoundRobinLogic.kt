package com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer

import android.util.Log
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.Sample
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.SampleCoords
import java.util.concurrent.ConcurrentLinkedQueue

class SimpleRoundRobinLogic : RoundRobinLogic {
    private var history: ConcurrentLinkedQueue<Sample> = ConcurrentLinkedQueue<Sample>()

    override fun computeID(incomingLayer: VelocityLayer): SampleCoords {
        if (!history.isEmpty()) {
            if (incomingLayer.getLayerNumber() !=
                history.last().getSampleCoords().getLayerNumber()
            ) {
                return incomingLayer.getSampleIDByInt(1)
            }
        }else if (history.size > 3) history.remove()
        val totalRR = incomingLayer.getSampleIDByInt(1).getRoundRobinCount()
        val newRR: Int = (Math.random() * ((totalRR - 1) + 1) + 1).toInt()
        val outgoingSampleID = incomingLayer.getSampleIDByInt(newRR)
        history.add(incomingLayer.getSampleBySampleCoords(outgoingSampleID))
        Log.i("SampleID", "${ outgoingSampleID.getGroupNumber() } -- ${outgoingSampleID.getLayerNumber()} -- ${outgoingSampleID.getRoundRobinNumber()}")
        return outgoingSampleID
    }

    override fun addToHistory(sample: Sample) {
        history.add(sample)

    }
}

