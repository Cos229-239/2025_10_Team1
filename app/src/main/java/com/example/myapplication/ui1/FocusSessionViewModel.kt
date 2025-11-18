package com.example.myapplication.ui1

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class FocusSessionViewModel : ViewModel() {
    // TODO: Implement FocusSessionViewModel
    val state = MutableStateFlow(FocusState())
    fun setWheelMinutes(minutes: Int) {}
    fun setWheelSeconds(seconds: Int) {}
    fun startBreak() {}
    fun fullFocus() {}
    fun toggleSilence() {}
    fun toggleRun() {}
    fun resetSession() {}
    fun addNote(note: String) {}
    fun deleteNoteAt(index: Int) {}
}

data class FocusState(
    val progress: Float = 0f,
    val wheelMinutes: Int = 25,
    val wheelSeconds: Int = 0,
    val isRunning: Boolean = false,
    val currentTask: String = "",
    val isSilenceOn: Boolean = false,
    val notes: List<String> = emptyList()
)
