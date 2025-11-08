package ru.otus.arch.state

import ru.otus.arch.data.AppData
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState
import kotlin.coroutines.suspendCoroutine
import kotlin.test.Test

internal class AddingUserStateTest : BaseStateTest() {

    fun createState() = AddingUserState(
        context,
        AppData(),
        PROFILE_1,
        addUser
    )

    @Test
    fun addsUserAndReturnsToList() = test {
        everySuspending { addUser.invoke(isAny()) } returns USER_1
        every { stateFactory.userList(isAny()) } returns nextState

        createState().start(stateMachine)

        verifyWithSuspend(exhaustive = false, inOrder = true) {
            stateMachine.setUiState(AppUiState.Loading)
            addUser.invoke(PROFILE_1)
            stateFactory.userList(isAny())
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun transfersToErrorIfAddFails() = test {
        val error = Exception("error")
        everySuspending { addUser.invoke(isAny()) } runs {
            throw error
        }
        every { stateFactory.addingUserError(isAny(), isAny(), isAny()) } returns nextState

        createState().start(stateMachine)

        verifyWithSuspend(exhaustive = false, inOrder = true) {
            stateMachine.setUiState(AppUiState.Loading)
            called { addUser.invoke(PROFILE_1) }
            stateFactory.addingUserError(isAny(), isEqual(PROFILE_1), isEqual(error))
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun returnsToFormOnBack() = test {
        everySuspending { addUser.invoke(isAny()) } runs {
            suspendCoroutine {
                // NO-OP
            }
        }
        every { stateFactory.addUserForm(isAny(), isAny()) } returns nextState

        val state = createState()
        state.start(stateMachine)
        state.process(AppGesture.Back)

        verifyWithSuspend(exhaustive = false, inOrder = true) {
            stateFactory.addUserForm(isAny(), isEqual(PROFILE_1))
            stateMachine.setMachineState(nextState)
        }
    }
}