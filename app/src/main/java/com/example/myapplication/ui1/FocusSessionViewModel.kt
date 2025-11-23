package com.example.myapplication.ui1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// --- State Holder for the Focus Screen ---
data class FocusSessionState(
    val wheelMinutes: Int = 25,
    val wheelSeconds: Int = 0,
    val totalSessionTime: Long = 25 * 60,
    val timeRemaining: Long = 25 * 60,
    val isRunning: Boolean = false,
    val isSilenceOn: Boolean = false,
    val currentTask: String = "Studying for finals",
    val notes: List<String> = emptyList(),
) {
    val progress: Float
        get() = if (totalSessionTime > 0) {
            (1f - timeRemaining.toFloat() / totalSessionTime.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }
}

// --- ViewModel for the Focus Screen ---
class FocusSessionViewModel : ViewModel() {

    private val _state = MutableStateFlow(FocusSessionState())
    val state: StateFlow<FocusSessionState> = _state.asStateFlow()

    private var timerJob: Job? = null

    fun setWheelMinutes(minutes: Int) {
        if (!_state.value.isRunning) {
            val newTotalTime = minutes * 60L + _state.value.wheelSeconds
            _state.update { it.copy(wheelMinutes = minutes, totalSessionTime = newTotalTime, timeRemaining = newTotalTime) }
        }
    }

    fun setWheelSeconds(seconds: Int) {
        if (!_state.value.isRunning) {
            val newTotalTime = _state.value.wheelMinutes * 60L + seconds
            _state.update { it.copy(wheelSeconds = seconds, totalSessionTime = newTotalTime, timeRemaining = newTotalTime) }
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
        _state.update { it.copy(isRunning = true) }
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_state.value.timeRemaining > 0) {
                delay(1000)
                _state.update { it.copy(timeRemaining = it.timeRemaining - 1) }
            }
            _state.update { it.copy(isRunning = false) } // Timer finished
        }
    }

    private fun pauseSession() {
        _state.update { it.copy(isRunning = false) }
        timerJob?.cancel()
    }

    fun resetSession() {
        pauseSession()
        _state.update { it.copy(timeRemaining = it.totalSessionTime) }
    }

    fun startBreak() {
        // For now, a break is just a 5-minute session
        _state.update { it.copy(totalSessionTime = 5 * 60, timeRemaining = 5 * 60) }
        startSession()
    }

    fun fullFocus() {
        // Placeholder for a more complex full focus mode
    }

    fun toggleSilence() {
        _state.update { it.copy(isSilenceOn = !it.isSilenceOn) }
    }

    fun addNote(note: String) {
        if (note.isNotBlank()) {
            _state.update { it.copy(notes = it.notes + note) }
        }
    }

    fun deleteNoteAt(index: Int) {
        _state.update { state ->
            state.copy(notes = state.notes.toMutableList().also { it.removeAt(index) })
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel() // Ensure coroutine is cancelled when ViewModel is cleared
    }
}
