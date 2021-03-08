/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.features

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.utils.TimeUtils

internal class CountDownTimerViewModel : ViewModel() {

    // region Members

    private val _durationInMinutesAndSeconds = MutableLiveData("00:00")
    val durationInMinutesAndSeconds: LiveData<String> = _durationInMinutesAndSeconds

    private val _timerState = MutableLiveData(CountDownTimerState.IDLE)
    val timerState: LiveData<CountDownTimerState> = _timerState

    private var tickTocTimer: TicTocTimer? = null
    private var remainingDurationInMilliSeconds:Long = 0L

    // endregion

    // region Public Api

    fun startCountDownTimer(durationInMilliseconds: Long) {
        createTicTocTimer(durationInMilliseconds = durationInMilliseconds)
        tickTocTimer?.start()
        updateTimerState(state = CountDownTimerState.IN_PROGRESS)
    }

    fun pauseCountDownTimer() {
        tickTocTimer?.cancel()
        tickTocTimer = null
        updateTimerState(state = CountDownTimerState.PAUSED)
    }

    fun resumeCountDownTimer() {
        updateTimerState(state = CountDownTimerState.IN_PROGRESS)
        createTicTocTimer(remainingDurationInMilliSeconds)
        tickTocTimer?.start()
    }

    fun stopCountDownTimer() {
        tickTocTimer?.cancel()
        tickTocTimer = null
        updateDuration(durationLeftInMilliseconds = 0L)
        updateTimerState(state = CountDownTimerState.STOPPED)
    }

    // endregion

    // region Private Api

    private fun updateTimerState(state: CountDownTimerState) {
        _timerState.value = state
    }

    private fun createTicTocTimer(durationInMilliseconds: Long) {
        tickTocTimer = TicTocTimer(
            durationInMilliseconds = durationInMilliseconds,
            onCountdownTick = { durationLeftInMilliseconds ->
                updateDuration(durationLeftInMilliseconds = durationLeftInMilliseconds)
            },
            onCountdownFinished = {
                updateTimerState(state = CountDownTimerState.IDLE)
            }
        )
    }

    private fun updateDuration(durationLeftInMilliseconds: Long) {
        val formattedDuration = TimeUtils.formatMillisecondsToMinutesAndSeconds(
            durationInMilliseconds = durationLeftInMilliseconds
        )
        _durationInMinutesAndSeconds.value = formattedDuration
        remainingDurationInMilliSeconds = durationLeftInMilliseconds
    }

    // endregion
}
