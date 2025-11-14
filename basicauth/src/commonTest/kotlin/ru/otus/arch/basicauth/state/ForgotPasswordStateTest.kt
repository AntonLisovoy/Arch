package ru.otus.arch.basicauth.state

import ru.otus.arch.basicauth.data.BasicAuthGesture
import ru.otus.arch.basicauth.data.BasicAuthUiState
import kotlin.test.Test

internal class ForgotPasswordStateTest : BaseStateTest() {

    private fun createState(error: Throwable? = null) = ForgotPasswordState(
        context,
        error
    )

    @Test
    fun rendersHardcodedCredentialsOnStart() = test {
        createState().start(stateMachine)

        verify(exhaustive = false, inOrder = true) {
            stateMachine.setUiState(isEqual(
                BasicAuthUiState.ForgotPassword(
                    username = "admin",
                    password = "password"
                )
            ))
        }
    }

    @Test
    fun rendersHardcodedCredentialsWithError() = test {
        val error = RuntimeException("Test error")
        createState(error).start(stateMachine)

        verify(exhaustive = false, inOrder = true) {
            stateMachine.setUiState(isEqual(
                BasicAuthUiState.ForgotPassword(
                    username = "admin",
                    password = "password"
                )
            ))
        }
    }

    @Test
    fun navigatesToLoginOnBack() = test {
        every { stateFactory.start(isAny()) } returns nextState

        val state = createState()
        state.start(stateMachine)
        state.process(BasicAuthGesture.Back)

        verify(exhaustive = false, inOrder = false) {
            stateFactory.start(null)
            stateMachine.setMachineState(isAny())
        }
    }

    @Test
    fun navigatesToLoginOnAction() = test {
        every { stateFactory.start(isAny()) } returns nextState

        val state = createState()
        state.start(stateMachine)
        state.process(BasicAuthGesture.Action)

        verify(exhaustive = false, inOrder = false) {
            stateFactory.start(null)
            stateMachine.setMachineState(isAny())
        }
    }

    @Test
    fun preservesErrorWhenNavigatingBack() = test {
        val error = RuntimeException("Test error")
        every { stateFactory.start(isAny()) } returns nextState

        val state = createState(error)
        state.start(stateMachine)
        state.process(BasicAuthGesture.Back)

        verify(exhaustive = false, inOrder = false) {
            stateFactory.start(error)
            stateMachine.setMachineState(isAny())
        }
    }

    @Test
    fun preservesErrorWhenNavigatingOnAction() = test {
        val error = RuntimeException("Test error")
        every { stateFactory.start(isAny()) } returns nextState

        val state = createState(error)
        state.start(stateMachine)
        state.process(BasicAuthGesture.Action)

        verify(exhaustive = false, inOrder = false) {
            stateFactory.start(error)
            stateMachine.setMachineState(isAny())
        }
    }
}
