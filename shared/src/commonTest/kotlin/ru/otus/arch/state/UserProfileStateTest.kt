package ru.otus.arch.state

import kotlinx.coroutines.flow.flowOf
import ru.otus.arch.data.AppData
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState
import kotlin.test.Test

internal class UserProfileStateTest : BaseStateTest() {

    fun createState() = UserProfileState(
        context,
        AppData(),
        PROFILE_1.userId,
        loadProfile
    )

    @Test
    fun loadsProfile() = test {
        every { loadProfile.invoke(isAny()) } returns flowOf(PROFILE_1)

        createState().start(stateMachine)

        verifyWithSuspend(exhaustive = false, inOrder = true) {
            stateMachine.setUiState(AppUiState.Loading)
            loadProfile.invoke(isEqual(PROFILE_1.userId))
            stateMachine.setUiState(
                isEqual(
                    AppUiState.UserProfile(PROFILE_1)
                )
            )
        }
    }

    @Test
    fun transfersToErrorIfLoadFails() = test {
        val error = Exception("error")
        every { loadProfile.invoke(isAny()) } runs {
            throw error
        }
        every { stateFactory.userProfileError(isAny(), isAny(), isAny()) } returns nextState

        createState().start(stateMachine)

        verifyWithSuspend(exhaustive = false, inOrder = true) {
            stateMachine.setUiState(AppUiState.Loading)
            called { loadProfile.invoke(isEqual(PROFILE_1.userId)) }
            stateFactory.userProfileError(isAny(), isEqual(PROFILE_1.userId), isEqual(error))
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun transfersToDeletionOnDelete() = test {
        every { loadProfile.invoke(isAny()) } returns flowOf(PROFILE_1)
        every { stateFactory.deletingUser(isAny(), isAny()) } returns nextState

        val state = createState()
        state.start(stateMachine)
        state.process(AppGesture.DeleteUser)

        verifyWithSuspend(exhaustive = false, inOrder = true) {
            stateFactory.deletingUser(isAny(), isEqual(PROFILE_1.userId))
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun returnsToListOnBack() = test {
        every { loadProfile.invoke(isAny()) } returns flowOf(PROFILE_1)
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