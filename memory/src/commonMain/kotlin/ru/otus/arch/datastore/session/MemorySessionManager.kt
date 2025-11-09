package ru.otus.arch.datastore.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.otus.arch.domain.session.SessionManager
import ru.otus.arch.domain.session.data.Session

class MemorySessionManager : SessionManager {
    private val _loginData = MutableStateFlow<Session>(Session.NONE)

    override val session: StateFlow<Session> get() = _loginData.asStateFlow()

    override suspend fun login(username: String, password: String) {
        _loginData.value = Session.Active.Basic(username, password)
    }

    override suspend fun logout() {
        _loginData.value = Session.NONE
    }
}