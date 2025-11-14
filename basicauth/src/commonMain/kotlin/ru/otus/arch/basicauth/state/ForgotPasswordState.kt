package ru.otus.arch.basicauth.state

import ru.otus.arch.basicauth.data.BasicAuthGesture
import ru.otus.arch.basicauth.data.BasicAuthUiState

internal class ForgotPasswordState(
    context: BasicAuthContext,
    private val error: Throwable?
) : BasicAuthState(context) {

    override fun doStart() {
        render()
    }

    override fun doProcess(gesture: BasicAuthGesture) {
        when(gesture) {
            BasicAuthGesture.Back -> {
                setMachineState(factory.start(error))
            }
            BasicAuthGesture.Action -> {
                setMachineState(factory.start(error))
            }
            else -> super.doProcess(gesture)
        }
    }

    private fun render() {
        setUiState(BasicAuthUiState.ForgotPassword(
            username = USERNAME,
            password = PASSWORD
        ))
    }

    class Factory {
        operator fun invoke(context: BasicAuthContext, error: Throwable?) = ForgotPasswordState(
            context,
            error
        )
    }

    companion object {
        private const val USERNAME = "admin"
        private const val PASSWORD = "password"
    }
}
