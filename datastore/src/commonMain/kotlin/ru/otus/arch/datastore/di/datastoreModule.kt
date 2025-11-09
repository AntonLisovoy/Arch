package ru.otus.arch.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import ru.otus.arch.datastore.session.DatastoreSessionManager
import ru.otus.arch.datastore.session.SessionSerializer
import ru.otus.arch.domain.files.FilePathProvider
import ru.otus.arch.domain.session.SessionManager
import ru.otus.arch.domain.session.data.Session

val datastoreModule = DI.Module("datastore") {
    bindSingleton<DataStore<Session>> {
        DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = FileSystem.SYSTEM,
                serializer = SessionSerializer(instance()),
                producePath = {
                    instance<FilePathProvider>().getUserFilePath("session.ds").toPath()
                }
            )
        )
    }

    bindSingleton<SessionManager> { DatastoreSessionManager(instance(), instance("app")) }
}