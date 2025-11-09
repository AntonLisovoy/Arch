package ru.otus.arch.datastore.session

import androidx.datastore.core.DataStore
import androidx.datastore.core.okio.OkioSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json
import okio.BufferedSink
import okio.BufferedSource
import okio.use
import ru.otus.arch.domain.session.SessionManager
import ru.otus.arch.domain.session.data.Session

internal class DatastoreSessionManager(
    private val storage: DataStore<Session>,
    scope: CoroutineScope
) : SessionManager {

    override val session: StateFlow<Session> = storage.data.stateIn(
        scope = scope,
        started = kotlinx.coroutines.flow.SharingStarted.Eagerly,
        initialValue = Session.NONE
    )

    override suspend fun login(username: String, password: String) {
        storage.updateData {
            Session.Active.Basic(username, password)
        }
    }

    override suspend fun logout() {
        storage.updateData {
            Session.NONE
        }
    }
}

internal class SessionSerializer(private val json: Json): OkioSerializer<Session> {
    override val defaultValue: Session = Session.NONE

    override suspend fun readFrom(source: BufferedSource): Session = json.decodeFromString<Session>(source.readUtf8())

    override suspend fun writeTo(t: Session, sink: BufferedSink) {
        sink.use {
            it.writeUtf8(json.encodeToString(Session.serializer(), t))
        }
    }
}
