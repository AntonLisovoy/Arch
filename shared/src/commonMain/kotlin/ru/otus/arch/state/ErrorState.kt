package ru.otus.arch.state

import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState

internal class ErrorState(
    context: AppContext,
    private val error: Throwable,
    private val onRetry: AppStateFactory.() -> AppState,
    private val onBack: AppStateFactory.() -> AppState
) : BaseAppState(context) {
    override fun doStart() {
        super.doStart()
        setUiState(AppUiState.Error(error, error.isFatal().not()))
    }

    override fun doProcess(gesture: AppGesture) {
        when(gesture) {
            AppGesture.Action -> {
                if (error.isFatal()) {
                    setMachineState(factory.terminated())
                } else {
                    setMachineState(factory.onRetry())
                }
            }
            AppGesture.Back -> {
                setMachineState(factory.onBack())
            }
            else -> super.doProcess(gesture)
        }
    }
}