package ru.otus.arch.state

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import ru.otus.arch.data.AppData
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState
import ru.otus.arch.net.data.Session
import kotlin.test.Test

internal class UserListStateTest : BaseStateTest() {

    fun createState(data: AppData = AppData()) = UserListState(
        context,
        data,
        sessionManager,
        loadUsers
    )

    @Test
    fun startsUpWithEmptyData() = test {
        every { sessionManager.session } returns flowOf(Session.NONE).stateIn(backgroundScope)
        every { loadUsers.invoke() } returns flowOf(listOf(USER_1))

        createState().start(stateMachine)

        verifyWithSuspend(exhaustive = false, inOrder = true) {
            stateMachine.setUiState(AppUiState.Loading)
            stateMachine.setUiState(
                isEqual(
                    AppUiState.UserList(
                        users = listOf(USER_1),
                        loggedIn = false
                    )
                )
            )
        }
    }

    @Test
    fun startsUpWithNonEmptyData() = test {
        every { sessionManager.session } returns flowOf(Session.NONE).stateIn(backgroundScope)
        every { loadUsers.invoke() } returns flowOf(listOf(USER_1, USER_2))

        createState(
            AppData(
                users = listOf(USER_1)
            )
        ).start(stateMachine)

        verifyWithSuspend(exhaustive = false, inOrder = true) {
            stateMachine.setUiState(
                isEqual(
                    AppUiState.UserList(
                        users = listOf(USER_1),
                        loggedIn = false
                    )
                )
            )
            stateMachine.setUiState(
                isEqual(
                    AppUiState.UserList(
                        users = listOf(USER_1, USER_2),
                        loggedIn = false
                    )
                )
            )
        }
    }

    @Test
    fun transfersToErrorIfLoadFails() = test {
        every { sessionManager.session } returns flowOf(Session.NONE).stateIn(backgroundScope)
        val error = Exception("error")
        every { loadUsers.invoke() } runs {
            throw error
        }
        every { stateFactory.userListError(isAny(), isAny()) } returns nextState

        createState().start(stateMachine)

        verifyWithSuspend(exhaustive = false, inOrder = true) {
            stateFactory.userListError(isAny(), isEqual(error))
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun transfersToLoginOnLogin() = test {
        every { sessionManager.session } returns flowOf(Session.NONE).stateIn(backgroundScope)
        every { loadUsers.invoke() } returns flowOf(emptyList())
        every { stateFactory.userListLogin(isAny()) } returns nextState

        val state = createState()
        state.start(stateMachine)
        state.process(AppGesture.Login)

        verifyWithSuspend(exhaustive = false, inOrder = true) {
            stateFactory.userListLogin(isAny())
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun transfersToUserSelectionOnSelect() = test {
        every { sessionManager.session } returns flowOf(Session.NONE).stateIn(backgroundScope)
        every { loadUsers.invoke() } returns flowOf(listOf(USER_1))
        every { stateFactory.userProfile(isAny(), isAny()) } returns nextState

        val state = createState()
        state.start(stateMachine)
        state.process(AppGesture.UserSelected(USER_1.userId))

        verifyWithSuspend(exhaustive = false, inOrder = true) {
            stateFactory.userProfile(isAny(), isEqual(USER_1.userId))
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun transfersToAddUserOnAddUser() = test {
        every { sessionManager.session } returns flowOf(Session.NONE).stateIn(backgroundScope)
        every { loadUsers.invoke() } returns flowOf(listOf(USER_1))
        every { stateFactory.addUserForm(isAny(), isAny()) } returns nextState

        val state = createState()
        state.start(stateMachine)
        state.process(AppGesture.AddUser)

        verifyWithSuspend(exhaustive = false, inOrder = true) {
            stateFactory.addUserForm(isAny(), isAny())
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun logsOutOnLogout() = test {
        every { sessionManager.session } returns flowOf(SESSION_BASIC).stateIn(backgroundScope)
        every { loadUsers.invoke() } returns flowOf(emptyList())
        everySuspending { sessionManager.logout() } returns Unit

        val state = createState()
        state.start(stateMachine)
        state.process(AppGesture.Logout)

        verifyWithSuspend(exhaustive = false, inOrder = true) {
            sessionManager.session
            sessionManager.logout()
        }
    }

    @Test
    fun terminatesOnBack() = test {
        every { sessionManager.session } returns flowOf(Session.NONE).stateIn(backgroundScope)
        every { loadUsers.invoke() } returns flowOf(emptyList())
        every { stateFactory.terminated() } returns nextState

        val state = createState()
        state.start(stateMachine)
        state.process(AppGesture.Back)

        verify(exhaustive = false, inOrder = true) {
            stateFactory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }
}