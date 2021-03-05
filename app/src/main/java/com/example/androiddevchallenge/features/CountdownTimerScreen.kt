package com.example.androiddevchallenge.features

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CountDownTimerScreen(countDownTimerViewModel: CountDownTimerViewModel = viewModel()) {
    // TODO Have a countdown timer that counts down from set specified time
    // TODO Animate change from seconds to minutes to hours
    // TODO Animate when it starts and ends or almost ending
    // TODO Pause or stop the timer
    // TODO Reset the timer
    // TODO Write Unit/Integration Test
    // TODO Use custom fonts for text ,have 24 series vibe
    // TODO Have sounds for timer?
    // TODO Timer that is indeterminate or determinate i.e set time or just adds on to the time?
    // TODO Look for a design that sums up my thoughts
    // TODO Start timer manually
    // TODO Extend functionality to hours

    val duration: String by countDownTimerViewModel.durationInMinutesAndSeconds.observeAsState("")
    val isFinished: Boolean by countDownTimerViewModel.isFinished.observeAsState(false)


    Text(
        text = duration,
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h3
    )
}