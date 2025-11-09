package ru.otus.arch.datastore.session

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import ru.otus.arch.domain.session.SessionManager
import ru.otus.arch.domain.session.data.Session
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MemorySessionManagerTest {
    private lateinit var dispatcher: TestDispatcher
    private lateinit var manager: SessionManager

    @BeforeTest
    fun init() {
        dispatcher = UnconfinedTestDispatcher()
        manager = MemorySessionManager()
    }

    private fun test(body: suspend TestScope.() -> Unit) = runTest(dispatcher, testBody = body)

    @Test
    fun startsWithEmptySession() = test {
        assertEquals(
            Session.NONE,
            manager.session.first()
        )
    }

    @Test
    fun changesSessionWhenLoggedIn() = test {
        val username = "user"
        val password = "password"
        val sessions = mutableListOf<Session>()
        backgroundScope.launch {
            manager.session.collect { sessions.add(it) }
        }

        manager.login(username, password)

        assertEquals(2, sessions.size)
        assertEquals(Session.NONE, sessions[0])
        assertEquals(Session.Active.Basic(username, password), sessions[1])
    }

    @Test
    fun changesSessionWhenLoggedOut() = test {
        val username = "user"
        val password = "password"
        val sessions = mutableListOf<Session>()
        backgroundScope.launch {
            manager.session.collect { sessions.add(it) }
        }

        manager.login(username, password)
        manager.logout()

        assertEquals(3, sessions.size)
        assertEquals(Session.NONE, sessions[0])
        assertEquals(Session.Active.Basic(username, password), sessions[1])
        assertEquals(Session.NONE, sessions[0])
    }
}