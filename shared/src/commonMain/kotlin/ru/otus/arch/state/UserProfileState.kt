package ru.otus.arch.state

import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import ru.otus.arch.data.AppData
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState
import ru.otus.arch.data.Profile
import ru.otus.arch.domain.net.usecase.LoadUserProfile

internal class UserProfileState(
    context: AppContext,
    private val data: AppData,
    private val id: Int,
    private val loadProfile: LoadUserProfile
) : BaseAppState(context) {

    override fun doStart() {
        render()
        load()
    }

    private fun load() = stateScope.launch {
        setUiState(AppUiState.Loading)
        try {
            loadProfile(id).collect {
                render(it)
            }
        } catch (e: Throwable) {
            ensureActive()
            setMachineState(factory.userProfileError(data, id, e))
        }
    }

    override fun doProcess(gesture: AppGesture) {
        when(gesture) {
            is AppGesture.Back -> {
                setMachineState(factory.userList(data))
            }
            is AppGesture.DeleteUser -> {
                setMachineState(factory.deletingUser(data, id))
            }
            else -> super.doProcess(gesture)
        }
    }

    private fun render(profile: Profile? = null) {
        if (null == profile) {
            setUiState(AppUiState.Loading)
        } else {
            setUiState(AppUiState.UserProfile(profile))
        }
    }

    class Factory(private val loadProfile: LoadUserProfile) {
        operator fun invoke(context: AppContext, data: AppData, id: Int) = UserProfileState(
            context,
            data,
            id,
            loadProfile
        )
    }
}
