package ru.otus.arch.datastore.session

import androidx.datastore.core.DataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.updateAndGet
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
class DatastoreSessionManagerTest{

    private lateinit var dispatcher: TestDispatcher
    private lateinit var manages: SessionManager

    lateinit var storage: DataStore<Session>

    @BeforeTest
    fun init() {
        dispatcher = UnconfinedTestDispatcher()
        storage = object : DataStore<Session> {
            private val buffer = MutableStateFlow<Session>(Session.NONE)
            override val data: Flow<Session> = buffer.asSharedFlow()
            override suspend fun updateData(transform: suspend (Session) -> Session): Session {
                return buffer.updateAndGet { transform(it) }
            }
        }
    }

    private suspend fun TestScope.createManager(): SessionManager = DatastoreSessionManager(
        storage = storage,
        scope = backgroundScope
    )

    private fun test(body: suspend TestScope.() -> Unit) = runTest(dispatcher, testBody = body)

    @Test
    fun startsWithEmptySession() = test {
        val manager = createManager()

        assertEquals(
            Session.NONE,
            manager.session.first()
        )
    }

    @Test
    fun changesSessionWhenLoggedIn() = test {
        val username = "user"
        val password = "password"

        val manager = createManager()

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

        val manager = createManager()

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