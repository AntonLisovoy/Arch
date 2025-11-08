package ru.otus.arch.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.coroutines.CoroutineState
import io.github.aakira.napier.Napier
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState

internal typealias AppState = CommonMachineState<AppGesture, AppUiState>

internal abstract class BaseAppState(context: AppContext) : CoroutineState<AppGesture, AppUiState>(), AppContext by context {
    override fun doProcess(gesture: AppGesture) {
        Napier.w { "Unsupported gesture: $gesture" }
    }
}