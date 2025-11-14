package ru.otus.arch.state

import kotlinx.coroutines.launch
import ru.otus.arch.data.AppData
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState
import ru.otus.arch.data.Profile
import ru.otus.arch.domain.net.usecase.AddUser

internal class AddingUserState(
    context: AppContext,
    private val data: AppData,
    private val profile: Profile,
    private val addUser: AddUser
) : BaseAppState(context) {

    override fun doStart() {
        setUiState(AppUiState.Loading)
        add()
    }

    private fun add() = stateScope.launch {
        try {
            addUser(profile)
            setMachineState(factory.userList(data))
        } catch (e: Exception) {
            setMachineState(factory.addingUserError(data, profile, e))
        }
    }

    override fun doProcess(gesture: AppGesture) {
        when(gesture) {
            AppGesture.Back -> setMachineState(factory.addUserForm(data, profile))
            else -> super.doProcess(gesture)
        }
    }

    class Factory(private val addUser: AddUser) {
        operator fun invoke(context: AppContext, data: AppData, profile: Profile) = AddingUserState(
            context,
            data,
            profile,
            addUser
        )
    }
}