package ru.otus.arch.net.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.otus.arch.data.Profile
import ru.otus.arch.net.UsersApi

internal interface LoadUserProfile {
    operator fun invoke(id: Int): Flow<Profile>
}

internal class LoadUserProfileImpl(private val api: UsersApi) : LoadUserProfile {
    override fun invoke(id: Int): Flow<Profile> = flow {
        emit(api.loadUserProfile(id))
    }
}
