package ru.otus.arch.state

import ru.otus.arch.data.AppData
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState

/**
 * A stub for basic JS test
 */
internal class WelcomeState(context: AppContext) : BaseAppState(context) {
    override fun doStart() {
        super.doStart()
        setUiState(AppUiState.Welcome)
    }

    override fun doProcess(gesture: AppGesture) {
        when(gesture) {
            is AppGesture.Back -> {
                setMachineState(factory.terminated())
            }
            is AppGesture.Action -> {
                setMachineState(factory.userList(AppData()))
            }
            else -> super.doProcess(gesture)
        }
    }
}