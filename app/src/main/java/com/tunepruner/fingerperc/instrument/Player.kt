package com.tunepruner.fingerperc.instrument

import android.view.MotionEvent

interface Player {
    fun play(event: MotionEvent)
    fun tearDown()
}