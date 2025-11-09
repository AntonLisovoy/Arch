package ru.otus.arch.domain.session

import kotlinx.coroutines.flow.StateFlow
import ru.otus.arch.domain.session.data.Session

interface SessionManager {
    val session: StateFlow<Session>
    suspend fun login(username: String, password: String)
    suspend fun logout()
}