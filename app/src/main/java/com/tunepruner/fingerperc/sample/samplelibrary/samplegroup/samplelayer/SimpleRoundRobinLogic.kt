package com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer

import android.util.Log
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.Playable
import com.tunepruner.fingerperc.sample.samplelibrary.samplegroup.samplelayer.playable.SampleCoords
import java.util.concurrent.ConcurrentLinkedQueue

class SimpleRoundRobinLogic : RoundRobinLogic {
    private var history: ConcurrentLinkedQueue<Playable> = ConcurrentLinkedQueue<Playable>()

    override fun computeID(incomingLayer: SampleLayer): SampleCoords {
        if (!history.isEmpty()) {
            if (incomingLayer.getLayerNumber() !=
                history.last().getSampleCoords().getLayerNumber()
            ) {
                return incomingLayer.getSampleIDByInt(1)
            }
        }else if (history.size > 3) history.remove()
        val totalRR = incomingLayer.getSampleIDByInt(1).getRoundRobinCount()
        val newRR: Int = (Math.random() * ((totalRR - 1) + 1) + 1).toInt()
        var outgoingSampleID = incomingLayer.getSampleIDByInt(newRR)
        history.add(incomingLayer.getPlayableBySampleCoords(outgoingSampleID))
        Log.i("SampleID", "${ outgoingSampleID.getGroupNumber() } -- ${outgoingSampleID.getLayerNumber()} -- ${outgoingSampleID.getRoundRobinNumber()}")
        return outgoingSampleID
    }

    override fun addToHistory(playable: Playable) {
        history.add(playable)

    }
}

