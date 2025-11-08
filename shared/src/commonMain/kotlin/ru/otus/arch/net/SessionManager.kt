package ru.otus.arch.net

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.otus.arch.net.data.Session

interface SessionManager {
    val session: StateFlow<Session>
    suspend fun login(loginData: Session.Active)
    suspend fun logout()
}

class SessionManagerImpl : SessionManager {

    private val _loginData = MutableStateFlow<Session>(Session.NONE)

    override val session: StateFlow<Session> get() = _loginData.asStateFlow()

    override suspend fun login(loginData: Session.Active) {
        _loginData.value = loginData
    }

    override suspend fun logout() {
        _loginData.value = Session.NONE
    }
}