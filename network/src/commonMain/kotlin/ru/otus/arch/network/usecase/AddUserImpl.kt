package ru.otus.arch.network.usecase

import ru.otus.arch.data.Profile
import ru.otus.arch.data.User
import ru.otus.arch.domain.net.usecase.AddUser
import ru.otus.arch.network.api.UsersApi

internal class AddUserImpl(private val api: UsersApi) : AddUser {
    override suspend fun invoke(profile: Profile): User = api.addUser(profile)
}
