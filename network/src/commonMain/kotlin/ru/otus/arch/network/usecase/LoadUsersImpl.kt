package ru.otus.arch.network.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.otus.arch.data.User
import ru.otus.arch.domain.net.usecase.LoadUsers
import ru.otus.arch.network.api.UsersApi

internal class LoadUsersImpl(private val api: UsersApi) : LoadUsers {
    override fun invoke(): Flow<List<User>> = flow {
        emit(api.loadUsers())
    }
}
