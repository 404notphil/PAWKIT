package com.tunepruner.fingerperc.instrument

import android.graphics.PointF
import android.util.Log
import android.view.MotionEvent
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.properties.Delegates

class SimpleTouchLogic : TouchLogic {
    private val returns: ConcurrentLinkedQueue<PointF> = ConcurrentLinkedQueue<PointF>()
    var lastTime = Calendar.getInstance().timeInMillis;

    override fun reduceTouchEvent(event: MotionEvent): PointF? {
        Log.i("SimpleTouchLogic", "${event.y}")
        val pointerIndex = event.actionIndex
        event.getPointerId(pointerIndex)
        val maskedAction = event.actionMasked

        val pointF = PointF()
        when (maskedAction) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                pointF.x = event.getRawX(pointerIndex)
                pointF.y = event.getRawY(pointerIndex) // experimenting. Uncomment afterwards

                returns.add(pointF)
                lastTime = Calendar.getInstance().timeInMillis;

                return pointF
            }
        }
        return null

    }

}
