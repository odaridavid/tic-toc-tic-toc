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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
    val formattedDuration: String by countDownTimerViewModel.formattedDuration.observeAsState("00:00")
    val remainingDuration: Long by countDownTimerViewModel.remainingDurationInMilliSeconds.observeAsState(
        0L
    )
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

                TicTocDurationText(duration = formattedDuration, remainingDuration = remainingDuration)
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
    duration: String,
    remainingDuration: Long
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
        targetValue = if (isLessThan10Seconds(remainingDuration = remainingDuration))
            MaterialTheme.colors.error
        else
            MaterialTheme.colors.onBackground
    )
    Text(
        text = duration,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha = if (isLessThan10Seconds(remainingDuration = remainingDuration)) alpha else 0.6f)
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
                    "Start Timer Icon" // TODO Support different locales
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

@Composable
private fun isLessThan10Seconds(remainingDuration: Long) = remainingDuration < 10_000L

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
