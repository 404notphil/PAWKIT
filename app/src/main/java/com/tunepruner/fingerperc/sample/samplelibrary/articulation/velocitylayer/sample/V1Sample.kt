package com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample

import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.SoundPool
import com.tunepruner.fingerperc.instrument.FileSnapshot
import kotlinx.coroutines.runBlocking
import java.util.*


class V1Sample(
    private val sampleCoords: SampleCoords,
    private val fileSnapshot: FileSnapshot
) :
    Sample {

    override fun getSampleCoords(): SampleCoords {
        return sampleCoords
    }

    override fun getIndex(): Int {
        return fileSnapshot.index
    }

}
