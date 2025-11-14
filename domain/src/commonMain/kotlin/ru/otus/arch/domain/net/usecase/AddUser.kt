package ru.otus.arch.domain.net.usecase

import ru.otus.arch.data.Profile
import ru.otus.arch.data.User

interface AddUser {
    suspend operator fun invoke(profile: Profile): User
}
