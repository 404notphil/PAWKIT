package com.tunepruner.fingerperc.instrument.logging

import com.google.firebase.analytics.FirebaseAnalytics
import com.tunepruner.fingerperc.instrument.InstrumentActivity
import java.io.File
import java.util.*

class UsageReportingService(val app: InstrumentActivity) {
    private var startTime: Long = 0
    val FIVE_MIN_USAGE_THRESHOLD = 300000
    val TWENTY_MIN_USAGE_THRESHOLD = 1200000
    val ONE_HR_USAGE_THRESHOLD = 3600000
    val FIVE_HR_USAGE_THRESHOLD = 18000000
    val firebaseAnalytics = FirebaseAnalytics.getInstance(app)

    init{
        startTime = Calendar.getInstance().timeInMillis
    }

    fun startClock(){
        startTime = Calendar.getInstance().timeInMillis
    }

    fun stopClock() {
        checkIfUserReachedNewTimeThreshold()
    }

    fun checkIfUserReachedNewTimeThreshold() {
        //get total seconds of use from file
        val file = File(app.filesDir, "usage_data.txt")
        val textFromFile: String = if (file.exists()) {
            file.readText()
        } else "0"

        val previousTotal = if (textFromFile.isEmpty()) 0 else textFromFile.toInt()

        val sessionDuration = Calendar.getInstance().timeInMillis - startTime

        val newTotal = sessionDuration + previousTotal
        file.writeText(newTotal.toString(), Charsets.UTF_8)


        val ENTERED_FIVE_MIN_BRACKET: Boolean =
            previousTotal < FIVE_MIN_USAGE_THRESHOLD &&
                    newTotal in FIVE_MIN_USAGE_THRESHOLD until TWENTY_MIN_USAGE_THRESHOLD
        val ENTERED_TWENTY_MIN_BRACKET: Boolean =
            previousTotal < TWENTY_MIN_USAGE_THRESHOLD &&
                    newTotal in TWENTY_MIN_USAGE_THRESHOLD until ONE_HR_USAGE_THRESHOLD
        val ENTERED_ONE_HOUR_BRACKET: Boolean = previousTotal < ONE_HR_USAGE_THRESHOLD &&
                newTotal in ONE_HR_USAGE_THRESHOLD until FIVE_HR_USAGE_THRESHOLD
        val ENTERED_FIVE_HOUR_BRACKET: Boolean =
            previousTotal < FIVE_HR_USAGE_THRESHOLD &&
                    newTotal > FIVE_HR_USAGE_THRESHOLD

        if (ENTERED_FIVE_MIN_BRACKET) {
            firebaseAnalytics.setUserProperty("over_5mins_of_playtime", (newTotal/1000).toString())
        } else if (ENTERED_TWENTY_MIN_BRACKET) {
            firebaseAnalytics.setUserProperty("over_20mins_of_playtime", (newTotal/1000).toString())
        } else if (ENTERED_ONE_HOUR_BRACKET) {
            firebaseAnalytics.setUserProperty("over_1hr_of_playtime", (newTotal/1000).toString())
        } else if (ENTERED_FIVE_HOUR_BRACKET) {
            firebaseAnalytics.setUserProperty("over_5hr_of_playtime", (newTotal/1000).toString())
        }
    }
}