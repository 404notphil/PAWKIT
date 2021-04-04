package com.tunepruner.fingerperc.instrument

import android.app.Activity
import android.view.MotionEvent

interface Player {
    fun play(event: MotionEvent, activity: Activity)
    fun tearDown()
    fun muteAll()
}