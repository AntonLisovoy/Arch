package ru.otus.arch.network.usecase

import ru.otus.arch.domain.net.usecase.DeleteUser
import ru.otus.arch.network.api.UsersApi

internal class DeleteUserImpl(private val api: UsersApi) : DeleteUser {
    override suspend fun invoke(id: Int) = api.deleteUser(id)
}
