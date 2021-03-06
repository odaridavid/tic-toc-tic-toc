package com.example.androiddevchallenge.features

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
internal fun CountDownTimerScreen(countDownTimerViewModel: CountDownTimerViewModel = viewModel()) {
    val duration: String by countDownTimerViewModel.durationInMinutesAndSeconds.observeAsState("00:00")
    val timerState: CountDownTimerState by countDownTimerViewModel.timerState.observeAsState(
        CountDownTimerState.IDLE
    )
    val animatedColor by animateColorAsState(
        targetValue = if (isLessThan10Seconds(duration = duration))
            MaterialTheme.colors.error
        else
            MaterialTheme.colors.onBackground
    )

    //TODO Animate circle blinking white in dark mode black in light mode when almost ending and filling it up

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Tic Toc",
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4
        )

        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = duration,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(alpha = 0.6f)
                    .padding(32.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3,
                color = animatedColor
            )
            Row() {
                Icon(
                    imageVector = handleState(
                        timerState = timerState,
                        onTimerInactive = {
                            Icons.Filled.PlayCircleFilled
                        },
                        onTimerActive = {
                            Icons.Filled.PauseCircleFilled
                        },
                        onTimerPaused = {
                            Icons.Filled.PlayCircleFilled
                        }
                    ),
                    contentDescription = handleState(
                        timerState = timerState,
                        onTimerInactive = {
                            "Start Timer Icon" //TODO Support different locales
                        },
                        onTimerActive = {
                            "Pause Timer Icon"
                        },
                        onTimerPaused = {
                            "Resume Timer Icon"
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
                                onTimerInactive = {
                                    countDownTimerViewModel.startCountDownTimer(
                                        durationInMilliseconds = 15_000L // TODO Be able to set a dynamic time
                                    )
                                },
                                onTimerActive = {
                                    countDownTimerViewModel.pauseCountDownTimer()
                                },
                                onTimerPaused = {
                                    countDownTimerViewModel.resumeCountDownTimer()
                                }
                            )
                        }
                )

                Icon(
                    imageVector = Icons.Filled.StopCircle,
                    contentDescription = "Stop Timer Icon",
                    modifier = Modifier
                        .padding(32.dp)
                        .height(48.dp)
                        .width(48.dp)
                        .clip(shape = CircleShape)
                        .clickable(
                            enabled = handleState(
                                timerState = timerState,
                                onTimerInactive = { false },
                                onTimerActive = { true },
                                onTimerPaused = { true }
                            )
                        ) {
                            handleState(
                                timerState = timerState,
                                onTimerActive = {
                                    countDownTimerViewModel.stopCountDownTimer()
                                },
                                onTimerInactive = {
                                    // Do Nothing
                                },
                                onTimerPaused = {
                                    countDownTimerViewModel.stopCountDownTimer()
                                }
                            )

                        }
                )
            }
        }
    }
}

// TODO Not hardcode this one off solution
@Composable
private fun isLessThan10Seconds(duration: String) = duration.contains("00:10") ||
        duration.contains("00:09") ||
        duration.contains("00:08") ||
        duration.contains("00:07") ||
        duration.contains("00:06") ||
        duration.contains("00:05") ||
        duration.contains("00:04") ||
        duration.contains("00:03") ||
        duration.contains("00:02") ||
        duration.contains("00:01")


internal fun <T> handleState(
    timerState: CountDownTimerState,
    onTimerInactive: () -> T,
    onTimerActive: () -> T,
    onTimerPaused: () -> T
): T = when (timerState) {
    CountDownTimerState.IDLE,
    CountDownTimerState.STOPPED -> onTimerInactive()
    CountDownTimerState.PAUSED -> onTimerPaused()
    CountDownTimerState.IN_PROGRESS -> onTimerActive()

}
