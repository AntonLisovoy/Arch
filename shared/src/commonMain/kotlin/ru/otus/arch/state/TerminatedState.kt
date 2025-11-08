package ru.otus.arch.state

import ru.otus.arch.data.AppUiState

class TerminatedState : AppState() {
    override fun doStart() {
        setUiState(AppUiState.Terminated)
    }
}