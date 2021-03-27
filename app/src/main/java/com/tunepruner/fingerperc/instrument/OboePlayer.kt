package com.tunepruner.fingerperc.instrument

import android.util.Log
import android.view.MotionEvent
import com.tunepruner.fingerperc.graphics.GUIManager
import com.tunepruner.fingerperc.gui.AnimationManager
import com.tunepruner.fingerperc.sample.SampleManager
import com.tunepruner.fingerperc.sample.samplelibrary.articulation.velocitylayer.sample.Sample
import com.tunepruner.fingerperc.zone.ZoneManager
import java.io.IOException
import java.util.*

class OboePlayer(
    private val touchLogic: TouchLogic,
    private val zoneManager: ZoneManager,
    private val sampleManager: SampleManager,
    private val animationManager: AnimationManager,
    private val resourceManager: ResourceManager

) : Player {
    private val jniPlayerAdapter = JNIPlayerAdapter()
    private val TAG = "Player"

    init {
        prepare()
        jniPlayerAdapter.loadAllAssets(resourceManager)
    }

    override fun play(event: MotionEvent) {
        val pointF = touchLogic.reduceTouchEvent(event)
        if (pointF != null) {
            val zoneLayer = zoneManager.computeVelocityLayer(pointF)
            val sample = sampleManager.computeSample(zoneLayer)
            animationManager.animate(zoneLayer)
            jniPlayerAdapter.play(sample)
        }
    }

    override fun tearDown() {
        jniPlayerAdapter.unloadWavAssetsNative()
        jniPlayerAdapter.teardownAudioStreamNative()
    }

    private fun prepare() {
        jniPlayerAdapter.setupAudioStreamNative(1)
        jniPlayerAdapter.startAudioStreamNative()
        jniPlayerAdapter.loadAllAssets(resourceManager)
    }
}

class JNIPlayerAdapter {
    private val tag = "JNIPlayerAdapter"
    fun play(sample: Sample) {
        trigger(sample.getIndex())
    }


    fun loadAllAssets(resourceManager: ResourceManager): Boolean {
        var allAssetsCorrect = true

        for (element in resourceManager.fileSnapshots) {
            allAssetsCorrect = loadWavAssetLocal(element) && allAssetsCorrect
        }

        return allAssetsCorrect
    }

    external fun setupAudioStreamNative(numChannels: Int)

    external fun startAudioStreamNative()

    external fun teardownAudioStreamNative()

    private fun loadWavAssetLocal(fileSnapshot: FileSnapshot): Boolean {
        var returnVal = false
        try {
            val assetFD = fileSnapshot.assetFileDescriptor
            val dataStream = assetFD.createInputStream()
            val dataLen = assetFD.length.toInt()
            val dataBytes = ByteArray(dataLen)
            dataStream.read(dataBytes, 0, dataLen)
            returnVal = loadWavAssetNative(dataBytes, 0, 0.5F, 1)
            assetFD.close()
        } catch (ex: IOException) {
            Log.i(tag, "IOException$ex")
        }
        return returnVal
    }

    private external fun loadWavAssetNative(
        wavBytes: ByteArray, index: Int, pan: Float, channels: Int
    ): Boolean

    external fun unloadWavAssetsNative()

    private external fun trigger(drumIndex: Int)

}