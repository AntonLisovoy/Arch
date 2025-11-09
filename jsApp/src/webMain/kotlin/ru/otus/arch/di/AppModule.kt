package ru.otus.arch.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import ru.otus.arch.datastore.session.MemorySessionManager
import ru.otus.arch.domain.session.SessionManager

val di = DI {
    bindSingleton<SessionManager> { MemorySessionManager() }
    import(sharedModule)
}
