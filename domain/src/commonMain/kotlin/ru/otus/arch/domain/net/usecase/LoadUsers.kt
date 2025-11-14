package ru.otus.arch.domain.net.usecase

import kotlinx.coroutines.flow.Flow
import ru.otus.arch.data.User

interface LoadUsers {
    operator fun invoke(): Flow<List<User>>
}
