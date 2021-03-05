package com.example.androiddevchallenge.features

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
internal fun CountDownTimerScreen(countDownTimerViewModel: CountDownTimerViewModel = viewModel()) {
    // TODO Animate when it starts and ends or almost ending
    // TODO Start ,Pause and Stop/Reset the timer
    // TODO Write Unit/Integration Test
    // TODO Have sounds for timer?
    // TODO Timer that is indeterminate or determinate i.e set time or just adds on to the time?
    // TODO Look for a design that sums up my thoughts, fix colors and fonts(have 24 series vibe) and alignments
    // TODO Extend functionality to hours

    val duration: String by countDownTimerViewModel.durationInMinutesAndSeconds.observeAsState("00:00")
    val timerState: CountDownTimerState by countDownTimerViewModel.timerState.observeAsState(
        CountDownTimerState.IDLE
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colors.background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = duration,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h3
        )

        Icon(
            imageVector = handleState(
                timerState = timerState,
                onTimerIdle = {
                    Icons.Filled.PlayCircleFilled
                },
                onTimerInProgress = {
                    Icons.Filled.PauseCircleFilled
                }
            ),
            contentDescription = handleState(
                timerState = timerState,
                onTimerIdle = {
                    "Start Timer Icon"
                },
                onTimerInProgress = {
                    "Pause Timer Icon"
                }
            ),
            modifier = Modifier
                .padding(32.dp)
                .height(48.dp)
                .width(48.dp)
                .clip(shape = CircleShape)
                .clickable {
                    handleState(
                        timerState = timerState,
                        onTimerIdle = {
                            countDownTimerViewModel.startCountDownTimer(durationInMilliseconds = 15_000L)
                        },
                        onTimerInProgress = {
                            countDownTimerViewModel.stopCountDownTimer()
                        }
                    )
                }
        )
    }
}

internal fun <T> handleState(
    timerState: CountDownTimerState,
    onTimerIdle: () -> T,
    onTimerInProgress: () -> T
): T = when (timerState) {
    CountDownTimerState.IDLE,
    CountDownTimerState.STOPPED -> onTimerIdle()
    CountDownTimerState.IN_PROGRESS -> onTimerInProgress()

}
