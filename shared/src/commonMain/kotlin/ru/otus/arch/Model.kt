package ru.otus.arch

import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import kotlinx.coroutines.flow.StateFlow
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState
import ru.otus.arch.state.AppStateFactory

class Model internal constructor(private val factory: AppStateFactory) {
    val stateMachine = FlowStateMachine(AppUiState.Loading) {
        factory.welcome()
    }

    val uiState: StateFlow<AppUiState> get() = stateMachine.uiState

    fun process(gesture: AppGesture) {
        stateMachine.process(gesture)
    }
}