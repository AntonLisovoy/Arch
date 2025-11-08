package ru.otus.arch.state

import kotlinx.coroutines.launch
import ru.otus.arch.data.AppData
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState
import ru.otus.arch.net.usecase.DeleteUser

internal class DeletingUserState(
    context: AppContext,
    private val data: AppData,
    private val id: Int,
    private val deleteUser: DeleteUser
) : BaseAppState(context) {

    override fun doStart() {
        setUiState(AppUiState.Loading)
        delete()
    }

    private fun delete() = stateScope.launch {
        try {
            deleteUser(id)
            setMachineState(factory.userList(data))
        } catch (e: Exception) {
            setMachineState(factory.deletingUserError(data, id, e))
        }
    }

    override fun doProcess(gesture: AppGesture) {
        when(gesture) {
            AppGesture.Back -> setMachineState(factory.userList(data))
            else -> super.doProcess(gesture)
        }
    }

    class Factory(private val deleteUser: DeleteUser) {
        operator fun invoke(context: AppContext, data: AppData, id: Int) = DeletingUserState(
            context,
            data,
            id,
            deleteUser
        )
    }
}