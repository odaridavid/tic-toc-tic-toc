package com.example.androiddevchallenge.features

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.utils.TimeUtils

class CountDownTimerViewModel : ViewModel() {

    // region Members

    private val _durationInMinutesAndSeconds = MutableLiveData("")
    val durationInMinutesAndSeconds: LiveData<String> = _durationInMinutesAndSeconds

    private val _isFinished = MutableLiveData(false)
    val isFinished: LiveData<Boolean> = _isFinished

    // endregion

    // region Init Blocks

    init {
        startCountDownTimer()
    }

    // endregion

    // region Private Api

    private fun startCountDownTimer() {
        TicTocTimer(
            durationInMilliseconds = 50_000,
            onCountdownTick = { durationLeftInMilliseconds ->
                val formattedDuration = TimeUtils.formatMillisecondsToMinutesAndSeconds(
                    durationInMilliseconds = durationLeftInMilliseconds
                )
                _durationInMinutesAndSeconds.value = formattedDuration
            },
            onCountdownFinished = {
                _isFinished.value = true
            }
        ).start()
    }

    // endregion
}