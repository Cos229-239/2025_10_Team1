package com.example.myapplication.ui1

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A composable that displays two number pickers for selecting minutes and seconds.
 * This version uses AnimatedContent to show a solid timer with a slide-up/down animation.
 *
 * @param minutes The currently selected minute value.
 * @param seconds The currently selected second value.
 * @param enabled Controls whether the user can change the values (not used in this version).
 * @param onMinutesChanged Callback that is invoked when the minute value changes (not used in this version).
 * @param onSecondsChanged Callback that is invoked when the second value changes (not used in this version).
 */
@Composable
fun TimerWheel(
    minutes: Int,
    seconds: Int,
    enabled: Boolean,
    onMinutesChanged: (Int) -> Unit,
    onSecondsChanged: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // Make the wheels slightly transparent if they are disabled when the timer is running
            .alpha(if (enabled) 1f else 0.9f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Animated number display for minutes
        AnimatedNumber(count = minutes)

        // Separator text
        Text(
            text = ":",
            style = MaterialTheme.typography.displayLarge,
            fontSize = 72.sp,
            modifier = Modifier.padding(horizontal = 8.dp) // Reduced padding for a tighter look
        )

        // Animated number display for seconds
        AnimatedNumber(count = seconds)
    }
}

/**
 * An animated text composable that slides vertically when its number changes.
 * This version has no shadow effect.
 *
 * @param count The number to display.
 */
@Composable
private fun AnimatedNumber(count: Int) {
    val numberStyle = MaterialTheme.typography.displayLarge.copy(fontSize = 72.sp)

    // A simple Box to contain the animated number.
    Box(
        contentAlignment = Alignment.Center
    ) {
        // --- THE SHADOW TEXT COMPOSABLE HAS BEEN REMOVED FROM HERE ---

        // AnimatedContent provides the sliding animation.
        AnimatedContent(
            targetState = count,
            transitionSpec = {
                // Animate sliding in from the bottom and out towards the top
                if (targetState > initialState) {
                    // Counting down (e.g., 25 -> 24)
                    slideInVertically { height -> height } togetherWith
                            slideOutVertically { height -> -height }
                } else {
                    // Counting up or resetting (e.g., 0 -> 25)
                    slideInVertically { height -> -height } togetherWith
                            slideOutVertically { height -> height }
                }
            },
            label = "animatedCount"
        ) { targetCount ->
            Text(
                text = targetCount.toString().padStart(2, '0'),
                style = numberStyle,
                textAlign = TextAlign.Center
            )
        }
    }
}



