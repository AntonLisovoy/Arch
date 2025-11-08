package ru.otus.arch.net.usecase

import ru.otus.arch.data.Profile
import ru.otus.arch.data.User
import ru.otus.arch.net.UsersApi

internal interface AddUser {
    suspend operator fun invoke(profile: Profile): User
}

internal class AddUserImpl(private val api: UsersApi) : AddUser {
    override suspend fun invoke(profile: Profile): User = api.addUser(profile)
}