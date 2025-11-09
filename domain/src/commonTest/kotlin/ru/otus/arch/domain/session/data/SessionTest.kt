package ru.otus.arch.domain.session.data

import kotlinx.serialization.json.Json
import ru.otus.arch.domainmock.SESSION_BASIC
import kotlin.test.Test
import kotlin.test.assertEquals

class SessionTest {
    @Test
    fun serializesNoneSession() {
        val session = Session.NONE
        assertEquals(
            session,
            Json.decodeFromString(Session.serializer(), Json.encodeToString(Session.serializer(), session))
        )
    }

    @Test
    fun serializesActiveBasicSession() {
        val session = SESSION_BASIC
        assertEquals(
            session,
            Json.decodeFromString(Session.serializer(), Json.encodeToString(Session.serializer(), session))
        )
    }
}