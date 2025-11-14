package ru.otus.arch.network.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.otus.arch.data.Profile
import ru.otus.arch.domain.net.usecase.LoadUserProfile
import ru.otus.arch.network.api.UsersApi

internal class LoadUserProfileImpl(private val api: UsersApi) : LoadUserProfile {
    override fun invoke(id: Int): Flow<Profile> = flow {
        emit(api.loadUserProfile(id))
    }
}
