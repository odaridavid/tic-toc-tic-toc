package com.example.androiddevchallenge.features

import android.util.Log
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
        // TODO Handle pause functionality here
        updateTimerState(state = CountDownTimerState.IN_PROGRESS)
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
    }

    // endregion
}