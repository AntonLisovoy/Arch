package ru.otus.arch.domain.net.usecase

import kotlinx.coroutines.flow.Flow
import ru.otus.arch.data.Profile

interface LoadUserProfile {
    operator fun invoke(id: Int): Flow<Profile>
}
