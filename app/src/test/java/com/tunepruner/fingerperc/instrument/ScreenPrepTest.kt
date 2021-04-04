package com.tunepruner.fingerperc.instrument

import android.app.Activity
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ScreenPrepTest {
private val activity: Activity = Activity()/*right now, field "windowManager" is null, so I can't use it in my tests*/
    @BeforeEach
    fun setUp() {
    }

    @Test
    fun getHeightReturnsPositiveInt(){
        assertNotNull(ScreenPrep.getDimensions(activity))
    }
}