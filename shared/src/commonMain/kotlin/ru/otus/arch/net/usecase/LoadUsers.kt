package ru.otus.arch.net.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.otus.arch.data.User
import ru.otus.arch.net.UsersApi

internal interface LoadUsers {
    operator fun invoke(): Flow<List<User>>
}

internal class LoadUsersImpl(private val api: UsersApi) : LoadUsers {
    override fun invoke(): Flow<List<User>> = flow {
        emit(api.loadUsers())
    }
}