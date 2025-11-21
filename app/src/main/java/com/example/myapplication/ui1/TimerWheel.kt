package com.example.myapplication.ui1

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Softer Figma-like colors
private val faded = Color(0xFFEB9D8F)   // light peachy pink for non-selected numbers
private val strong = Color(0xFF4A1234)  // deep plum for selected numbers

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimerWheel(
    minutes: Int,
    seconds: Int,
    enabled: Boolean,
    onMinutesChanged: (Int) -> Unit,
    onSecondsChanged: (Int) -> Unit
) {
    val items = (0..59).toList()

    // ListStates for each column
    val minuteState = rememberLazyListState(
        initialFirstVisibleItemIndex = minutes.coerceIn(0, 59)
    )
    val secondState = rememberLazyListState(
        initialFirstVisibleItemIndex = seconds.coerceIn(0, 59)
    )

    // What value is visually centered in each column right now?
    val selectedMinuteIndex by remember {
        derivedStateOf { centerIndexOrFallback(minuteState, minutes) }
    }
    val selectedSecondIndex by remember {
        derivedStateOf { centerIndexOrFallback(secondState, seconds) }
    }

    // Keep wheel in sync when ViewModel changes (e.g. reset, countdown start)
    LaunchedEffect(minutes) {
        if (!minuteState.isScrollInProgress) {
            minuteState.scrollToItem(minutes.coerceIn(0, 59))
        }
    }

    LaunchedEffect(seconds) {
        if (!secondState.isScrollInProgress) {
            secondState.scrollToItem(seconds.coerceIn(0, 59))
        }
    }

    // When user finishes scrolling, push the centered value back to the VM
    LaunchedEffect(minuteState.isScrollInProgress) {
        if (!minuteState.isScrollInProgress && enabled) {
            onMinutesChanged(selectedMinuteIndex)
        }
    }

    LaunchedEffect(secondState.isScrollInProgress) {
        if (!secondState.isScrollInProgress && enabled) {
            onSecondsChanged(selectedSecondIndex)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            WheelColumn(
                items = items,
                listState = minuteState,
                enabled = enabled,
                selectedIndex = selectedMinuteIndex
            )

            Spacer(modifier = Modifier.width(8.dp))

            WheelColumn(
                items = items,
                listState = secondState,
                enabled = enabled,
                selectedIndex = selectedSecondIndex
            )
        }

        // Center highlight + time text = *actual wheel selection*
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFF1C9A2).copy(alpha = 0.9f))
                .padding(horizontal = 32.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val mm = selectedMinuteIndex.coerceIn(0, 59)
                val ss = selectedSecondIndex.coerceIn(0, 59)

                Text(
                    text = mm.toString().padStart(2, '0'),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = strong
                )
                Text(
                    text = " : ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = strong
                )
                Text(
                    text = ss.toString().padStart(2, '0'),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = strong
                )
                Text(
                    text = " 00",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = strong
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WheelColumn(
    items: List<Int>,
    listState: LazyListState,
    enabled: Boolean,
    selectedIndex: Int
) {
    val fling = rememberSnapFlingBehavior(listState)

    LazyColumn(
        state = listState,
        flingBehavior = fling,              // always pass a non-null fling behavior
        modifier = Modifier
            .width(60.dp)
            .height(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        userScrollEnabled = enabled        // this actually disables scroll when running
    ) {
        itemsIndexed(items) { index, value ->
            Text(
                text = value.toString().padStart(2, '0'),
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = if (index == selectedIndex) strong else faded
            )
        }
    }
}

// Helper: try to get the visually-centered item; fall back to the VM value if needed
private fun centerIndexOrFallback(state: LazyListState, fallback: Int): Int {
    val visible = state.layoutInfo.visibleItemsInfo
    if (visible.isEmpty()) {
        return fallback.coerceIn(0, 59)
    }
    val centerItem = visible[visible.size / 2]
    return centerItem.index.coerceIn(0, 59)
}
