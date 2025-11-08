package ru.otus.arch.state

import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import ru.otus.arch.data.AppData
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState
import ru.otus.arch.net.SessionManager
import ru.otus.arch.net.data.Session
import ru.otus.arch.net.usecase.LoadUsers

internal class UserListState(
    context: AppContext,
    private var data: AppData,
    private val sessionManager: SessionManager,
    private val loadUsers: LoadUsers
) : BaseAppState(context) {
    override fun doStart() {
        render()
        subscribeSession()
        load()
    }

    private fun subscribeSession() = stateScope.launch {
        sessionManager.session.collect {
            render()
        }
    }

    private fun load() = stateScope.launch {
        try {
            loadUsers().collect {
                data = data.copy(users = it)
                render()
            }
        } catch (e: Throwable) {
            ensureActive()
            setMachineState(factory.userListError(data, e))
        }
    }

    override fun doProcess(gesture: AppGesture) {
        when(gesture) {
            is AppGesture.Back -> {
                setMachineState(factory.terminated())
            }
            is AppGesture.Login -> {
                setMachineState(factory.userListLogin(data))
            }
            is AppGesture.Logout -> {
                logout()
            }
            is AppGesture.UserSelected -> {
                setMachineState(factory.userProfile(data, gesture.userId))
            }
            is AppGesture.AddUser -> {
                setMachineState(factory.addUserForm(data))
            }
            else -> super.doProcess(gesture)
        }
    }

    private fun logout() = stateScope.launch {
        sessionManager.logout()
    }

    private fun render() {
        val users = data.users
        if (null == users) {
            setUiState(AppUiState.Loading)
        } else {
            setUiState(AppUiState.UserList(
                users = users,
                loggedIn = sessionManager.session.value is Session.Active
            ))
        }
    }

    class Factory(private val loadUsers: LoadUsers, private val sessionManager: SessionManager) {
        operator fun invoke(context: AppContext, data: AppData) = UserListState(
            context,
            data,
            sessionManager,
            loadUsers
        )
    }
}