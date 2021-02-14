package com.tunepruner.bomboleguerodemo.sample.samplelibrary.samplegroup.samplelayer.playable

import android.media.MediaPlayer

interface Playable {
    fun play()
    fun getSampleCoords(): SampleCoords
    fun getIndex(): Int
//    fun getSampleID(): SampleID

}