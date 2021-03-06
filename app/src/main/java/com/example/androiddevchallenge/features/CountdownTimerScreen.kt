package com.example.androiddevchallenge.features

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
internal fun CountDownTimerScreen(countDownTimerViewModel: CountDownTimerViewModel = viewModel()) {
    val duration: String by countDownTimerViewModel.durationInMinutesAndSeconds.observeAsState("00:00")
    val timerState: CountDownTimerState by countDownTimerViewModel.timerState.observeAsState(
        CountDownTimerState.IDLE
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TicTocHeader()

        val backgroundColor = MaterialTheme.colors.background
        val onBackgroundColor = MaterialTheme.colors.onBackground
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(contentAlignment = Alignment.Center) {
                Canvas(
                    modifier = Modifier
                        .width(258.dp)
                        .height(258.dp)
                ) {
                    drawCircle(
                        color = onBackgroundColor,
                    )
                }
                Canvas(
                    modifier = Modifier
                        .width(250.dp)
                        .height(250.dp)
                ) {
                    drawCircle(
                        color = backgroundColor,
                    )
                }

                TicTocDurationText(duration = duration)
            }
            TicTocActionButtons(
                timerState = timerState,
                countDownTimerViewModel = countDownTimerViewModel
            )
        }
    }
}

@Composable
private fun TicTocDurationText(
    duration: String
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val animatedColor by animateColorAsState(
        targetValue = if (isLessThan10Seconds(duration = duration))
            MaterialTheme.colors.error
        else
            MaterialTheme.colors.onBackground
    )
    Text(
        text = duration,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha = if (isLessThan10Seconds(duration = duration)) alpha else 0.6f)
            .padding(32.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h3,
        color = animatedColor
    )
}

@Composable
private fun TicTocActionButtons(
    timerState: CountDownTimerState,
    countDownTimerViewModel: CountDownTimerViewModel
) {
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
                    // TODO Show different color if disabled
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

@Composable
private fun TicTocHeader() {
    Text(
        text = "Tic Toc",
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h4
    )
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
