package com.example.androiddevchallenge

import com.example.androiddevchallenge.utils.TimeUtils
import junit.framework.Assert.assertEquals
import org.junit.Test

class TimeUtilsTest {

    @Test
    fun `when we format milliseconds to minutes and seconds, then expected output is received`() {
        // given a duration
        val duration = 60_000L

        // when we format the duration to minutes and seconds
        val actualValue = TimeUtils.formatMillisecondsToMinutesAndSeconds(
            durationInMilliseconds = duration
        )

        // then it matches the expected value
        assertEquals("01:00", actualValue)

    }
}