package com.example.myapplication.ui1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FocusSessionViewModel : ViewModel() {
    // Private mutable state for this ViewModel
    private val _state = MutableStateFlow(FocusState())
    // Public immutable state for the UI to observe
    val state = _state.asStateFlow()

    private var timerJob: Job? = null
    private var totalSessionTime: Long = 0

    // --- Wheel and Timer Controls ---

    fun setWheelMinutes(minutes: Int) {
        // Only allow changing the wheel when the timer is not running
        if (!_state.value.isRunning) {
            _state.update { it.copy(wheelMinutes = minutes) }
        }
    }

    fun setWheelSeconds(seconds: Int) {
        if (!_state.value.isRunning) {
            _state.update { it.copy(wheelSeconds = seconds) }
        }
    }

    fun toggleRun() {
        if (_state.value.isRunning) {
            pauseSession()
        } else {
            startSession()
        }
    }

    private fun startSession() {
        // Prevent multiple timers from running
        timerJob?.cancel()

        val currentTimeInSeconds = (_state.value.wheelMinutes * 60 + _state.value.wheelSeconds).toLong()

        // Set the total time for progress calculation, but only if it's a new session
        if (totalSessionTime == 0L || !_state.value.isRunning) {
            totalSessionTime = if (currentTimeInSeconds > 0) currentTimeInSeconds else 1 // Avoid division by zero
        }

        _state.update { it.copy(isRunning = true) }

        timerJob = viewModelScope.launch {
            var remainingSeconds = currentTimeInSeconds
            while (remainingSeconds > 0) {
                delay(1000)
                remainingSeconds--

                val minutes = (remainingSeconds / 60).toInt()
                val seconds = (remainingSeconds % 60).toInt()
                val progress = 1f - (remainingSeconds.toFloat() / totalSessionTime.toFloat())

                _state.update {
                    it.copy(
                        wheelMinutes = minutes,
                        wheelSeconds = seconds,
                        progress = progress.coerceIn(0f, 1f)
                    )
                }
            }
            // Timer finished
            _state.update { it.copy(isRunning = false, progress = 1f) }
        }
    }

    private fun pauseSession() {
        timerJob?.cancel()
        _state.update { it.copy(isRunning = false) }
    }

    fun resetSession() {
        timerJob?.cancel()
        totalSessionTime = 0
        // Reset state to its default initial values
        _state.value = FocusState()
    }

    // --- Actions ---

    fun startBreak() {
        timerJob?.cancel()
        totalSessionTime = 300 // 5 minutes in seconds
        _state.update {
            it.copy(
                wheelMinutes = 5,
                wheelSeconds = 0,
                currentTask = "On a Break",
                progress = 0f
            )
        }
        startSession()
    }

    fun fullFocus() {
        // Placeholder for future functionality like blocking notifications
        _state.update { it.copy(currentTask = "Deep Focus Activated") }
    }

    fun toggleSilence() {
        _state.update { it.copy(isSilenceOn = !it.isSilenceOn) }
        // You can add logic here to control the device's ringer mode
    }

    // --- Note Management ---

    fun addNote(note: String) {
        if (note.isNotBlank()) {
            _state.update {
                val updatedNotes = it.notes + note
                it.copy(notes = updatedNotes)
            }
        }
    }

    fun deleteNoteAt(index: Int) {
        _state.update {
            val updatedNotes = it.notes.toMutableList()
            if (index in updatedNotes.indices) {
                updatedNotes.removeAt(index)
            }
            it.copy(notes = updatedNotes)
        }
    }

    // --- Lifecycle ---
    override fun onCleared() {
        super.onCleared()
        // Stop the timer when the ViewModel is destroyed to prevent leaks
        timerJob?.cancel()
    }
}

// Data class defining the structure for this screen's state
data class FocusState(
    val progress: Float = 0f,
    val wheelMinutes: Int = 25,
    val wheelSeconds: Int = 0,
    val isRunning: Boolean = false,
    val currentTask: String = "Design UI for the new feature", // Example task
    val isSilenceOn: Boolean = false,
    val notes: List<String> = emptyList()
)
