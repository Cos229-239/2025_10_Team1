package com.example.myapplication.ui1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FocusUIState(
    val wheelMinutes: Int = 5,
    val wheelSeconds: Int = 0,
    val isRunning: Boolean = false,
    val progress: Float = 0f,
    val currentTask: String = "Finish Report",
    val notes: List<String> = emptyList(),
    val isSilenceOn: Boolean = false
)

class FocusSessionViewModel : ViewModel() {

    private val _state = MutableStateFlow(FocusUIState())
    val state: StateFlow<FocusUIState> = _state

    // Update wheel minutes (only if not running)
    fun setWheelMinutes(min: Int) {
        if (!_state.value.isRunning) {
            _state.value = _state.value.copy(
                wheelMinutes = min.coerceIn(0, 59)
            )
        }
    }

    // Update wheel seconds (only if not running)
    fun setWheelSeconds(sec: Int) {
        if (!_state.value.isRunning) {
            _state.value = _state.value.copy(
                wheelSeconds = sec.coerceIn(0, 59)
            )
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
        val snapshot = _state.value
        val totalSeconds =
            (snapshot.wheelMinutes * 60 + snapshot.wheelSeconds).coerceAtLeast(1)

        _state.value = snapshot.copy(isRunning = true)

        viewModelScope.launch {
            var remaining = totalSeconds

            while (remaining >= 0 && _state.value.isRunning) {
                val min = remaining / 60
                val sec = remaining % 60

                _state.value = _state.value.copy(
                    wheelMinutes = min,
                    wheelSeconds = sec,
                    progress = 1f - (remaining.toFloat() / totalSeconds.toFloat())
                )

                delay(1000L)
                remaining--
            }

            pauseSession()
        }
    }

    private fun pauseSession() {
        _state.value = _state.value.copy(isRunning = false)
    }

    fun addNote(note: String) {
        if (note.isNotBlank()) {
            _state.value = _state.value.copy(
                notes = _state.value.notes + note.trim()
            )
        }
    }

    fun deleteNoteAt(index: Int) {
        val currentNotes = _state.value.notes
        if (index in currentNotes.indices) {
            val updated = currentNotes.toMutableList().apply { removeAt(index) }
            _state.value = _state.value.copy(notes = updated)
        }
    }

    fun startBreak() {
        _state.value = _state.value.copy(currentTask = "Break Time")
    }

    fun fullFocus() {
        _state.value = _state.value.copy(currentTask = "Full Focus Mode")
    }

    fun toggleSilence() {
        _state.value = _state.value.copy(isSilenceOn = !_state.value.isSilenceOn)
    }

    fun resetSession() {
        _state.value = FocusUIState()
    }
}
