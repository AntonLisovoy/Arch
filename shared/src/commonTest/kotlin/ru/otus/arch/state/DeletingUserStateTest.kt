package ru.otus.arch.state

import ru.otus.arch.data.AppData
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState
import ru.otus.arch.domainmock.PROFILE_1
import kotlin.coroutines.suspendCoroutine
import kotlin.test.Test

internal class DeletingUserStateTest : BaseStateTest() {

    fun createState() = DeletingUserState(
        context,
        AppData(),
        PROFILE_1.userId,
        deleteUser
    )

    @Test
    fun deletesUserAndReturnsToList() = test {
        everySuspending { deleteUser.invoke(isAny()) } returns Unit
        every { stateFactory.userList(isAny()) } returns nextState

        createState().start(stateMachine)

        verifyWithSuspend(exhaustive = false, inOrder = true) {
            stateMachine.setUiState(AppUiState.Loading)
            deleteUser.invoke(PROFILE_1.userId)
            stateFactory.userList(isAny())
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun transfersToErrorIfDeleteFails() = test {
        val error = Exception("error")
        everySuspending { deleteUser.invoke(isAny()) } runs {
            throw error
        }
        every { stateFactory.deletingUserError(isAny(), isAny(), isAny()) } returns nextState

        createState().start(stateMachine)

        verifyWithSuspend(exhaustive = false, inOrder = true) {
            stateMachine.setUiState(AppUiState.Loading)
            called { deleteUser.invoke(PROFILE_1.userId) }
            stateFactory.deletingUserError(isAny(), isEqual(PROFILE_1.userId), isEqual(error))
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun returnsToUserListOnBack() = test {
        everySuspending { deleteUser.invoke(isAny()) } runs {
            suspendCoroutine {
                // NO-OP
            }
        }
        every { stateFactory.userList(isAny()) } returns nextState

        val state = createState()
        state.start(stateMachine)
        state.process(AppGesture.Back)

        verifyWithSuspend(exhaustive = false, inOrder = true) {
            stateFactory.userList(isAny())
            stateMachine.setMachineState(nextState)
        }
    }
}