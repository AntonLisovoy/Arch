package ru.otus.arch.net.usecase

import ru.otus.arch.net.UsersApi

internal interface DeleteUser {
    suspend operator fun invoke(id: Int)
}

internal class DeleteUserImpl(private val api: UsersApi) : DeleteUser {
    override suspend fun invoke(id: Int) = api.deleteUser(id)
}