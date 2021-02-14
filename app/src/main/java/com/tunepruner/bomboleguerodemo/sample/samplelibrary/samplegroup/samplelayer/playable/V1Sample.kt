package com.tunepruner.bomboleguerodemo.sample.samplelibrary.samplegroup.samplelayer.playable

import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.SoundPool
import com.tunepruner.bomboleguerodemo.instrument.FileSnapshot
import kotlinx.coroutines.runBlocking
import java.util.*


class V1Sample(
    private val sampleCoords: SampleCoords,
    private val assetFileDescriptor: AssetFileDescriptor,
    private val fileSnapshot: FileSnapshot
) :
    Playable {
    var soundID: Int = 0
    private val mediaHandler: MediaHandler = MediaHandler

    init {
        soundID = mediaHandler.addAsset(assetFileDescriptor)
    }

    override fun play() {
            mediaHandler.play(soundID)
    }

    override fun getSampleCoords(): SampleCoords {
        return sampleCoords
    }

    override fun getIndex(): Int {
        return fileSnapshot.index
    }

}

object MediaHandler {
    val audioAttributes = AudioAttributes.Builder()
        .setUsage(
            AudioAttributes.USAGE_ASSISTANCE_SONIFICATION
        )
        .setContentType(
            AudioAttributes.CONTENT_TYPE_SONIFICATION
        )
        .build()
    val soundPool = SoundPool.Builder()
        .setMaxStreams(20)
        .setAudioAttributes(
            audioAttributes
        )
        .build()
    private var soundIDs = LinkedList<Int>()


    fun addAsset(assetFileDescriptor: AssetFileDescriptor): Int {
        var soundID = soundPool.load(assetFileDescriptor, 1)
        soundIDs.add(soundID)
        return soundID
    }

    fun play(soundID: Int) {
        soundPool.play(soundID, 1F, 1F, 1, 0, 1F)
    }

    private fun playOnNewThread() = runBlocking {
//        val thread = async {
//            soundPool.play(soundID, 1F, 1F, 1, 0, 1F)
//        }
//        thread.await()
    }
}