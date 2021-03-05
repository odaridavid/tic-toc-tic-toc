package com.example.androiddevchallenge.features

import android.os.CountDownTimer

internal class TicTocTimer(
    durationInMilliseconds: Long,
    private val onCountdownTick: (durationLeftInMilliseconds: Long) -> Unit,
    private val onCountdownFinished: () -> Unit,
    tickInterval: Long = 1_000L
) : CountDownTimer(durationInMilliseconds, tickInterval) {

    override fun onTick(millisUntilFinished: Long) {
        onCountdownTick(millisUntilFinished)
    }

    override fun onFinish() {
        onCountdownFinished()
    }

}