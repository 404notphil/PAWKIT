package com.tunepruner.fingerperc.instrument.logging

import com.google.firebase.analytics.FirebaseAnalytics
import com.tunepruner.fingerperc.instrument.InstrumentActivity
import java.io.File
import java.util.*

class UsageReportingService(val app: InstrumentActivity) {
    private var startTime: Long = 0

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(app)

    companion object {
        const val FIVE_MIN_USAGE_THRESHOLD = 300000
        const val TWENTY_MIN_USAGE_THRESHOLD = 1200000
        const val ONE_HR_USAGE_THRESHOLD = 3600000
        const val FIVE_HR_USAGE_THRESHOLD = 18000000
    }

    init{
        startTime = Calendar.getInstance().timeInMillis
    }

    fun startClock(){
        startTime = Calendar.getInstance().timeInMillis
    }

    fun stopClock() {
        checkIfUserReachedNewTimeThreshold()
    }

    private fun checkIfUserReachedNewTimeThreshold() {
        //get total seconds of use from file
        val file = File(app.filesDir, "usage_data.txt")
        val textFromFile: String = if (file.exists()) {
            file.readText()
        } else "0"

        val previousTotal = if (textFromFile.isEmpty()) 0 else textFromFile.toInt()

        val sessionDuration = Calendar.getInstance().timeInMillis - startTime

        val newTotal = sessionDuration + previousTotal
        file.writeText(newTotal.toString(), Charsets.UTF_8)


        val enteredFiveMinuteBracket: Boolean =
            previousTotal < FIVE_MIN_USAGE_THRESHOLD &&
                    newTotal in FIVE_MIN_USAGE_THRESHOLD until TWENTY_MIN_USAGE_THRESHOLD
        val enteredTwentyMinuteBracket: Boolean =
            previousTotal < TWENTY_MIN_USAGE_THRESHOLD &&
                    newTotal in TWENTY_MIN_USAGE_THRESHOLD until ONE_HR_USAGE_THRESHOLD
        val enteredOneHourBracket: Boolean = previousTotal < ONE_HR_USAGE_THRESHOLD &&
                newTotal in ONE_HR_USAGE_THRESHOLD until FIVE_HR_USAGE_THRESHOLD
        val enteredFiveHourBracket: Boolean =
            FIVE_HR_USAGE_THRESHOLD in (previousTotal + 1) until newTotal

        when {
            enteredFiveMinuteBracket -> {
                firebaseAnalytics.setUserProperty("over_5mins_of_playtime", (newTotal/1000).toString())
            }
            enteredTwentyMinuteBracket -> {
                firebaseAnalytics.setUserProperty("over_20mins_of_playtime", (newTotal/1000).toString())
            }
            enteredOneHourBracket -> {
                firebaseAnalytics.setUserProperty("over_1hr_of_playtime", (newTotal/1000).toString())
            }
            enteredFiveHourBracket -> {
                firebaseAnalytics.setUserProperty("over_5hr_of_playtime", (newTotal/1000).toString())
            }
        }
    }
}