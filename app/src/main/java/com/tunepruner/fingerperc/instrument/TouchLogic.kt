package com.tunepruner.fingerperc.instrument

import android.graphics.PointF
import android.view.MotionEvent

interface TouchLogic {
    fun reduceTouchEvent(event: MotionEvent): PointF?


}
