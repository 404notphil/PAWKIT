package com.tunepruner.bomboleguerodemo.instrument

import android.graphics.Point
import android.graphics.PointF
import android.util.SparseArray
import android.view.MotionEvent
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.properties.Delegates

class SimpleTouchLogic : TouchLogic {
    private val returns: ConcurrentLinkedQueue<PointF> = ConcurrentLinkedQueue<PointF>()
    var lastTime = Calendar.getInstance().timeInMillis;
    var timeNow by Delegates.notNull<Long>()

    override fun reduceTouchEvent(event: MotionEvent): PointF? {
        val pointerIndex = event.actionIndex
        val pointerId = event.getPointerId(pointerIndex)
        val maskedAction = event.actionMasked

        val pointF: PointF = PointF()
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


//        val enoughTimePassed = timeNow - lastTime > 100
//
//        return if (isFarAway(event.x, event.y)) {
//        } else if (!isFarAway(event.x, event.y) &&
//            enoughTimePassed
//        ) {
//            returns.add(point)
//            lastTime = Calendar.getInstance().timeInMillis;
//            point
//        } else {
//            null
//        }
    }

    private fun isFarAway(x: Float, y: Float): Boolean {
        if (returns.isEmpty()) {
            return true
        }
        val side1 = x.toDouble() - returns.last().x
        val side2 = y.toDouble() - returns.last().y

        val distance = sqrt(side1.pow(2.0) + side2.pow(2.0))
        return distance > 500
    }
}
