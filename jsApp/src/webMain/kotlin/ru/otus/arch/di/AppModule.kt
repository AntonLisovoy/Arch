package ru.otus.arch.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import ru.otus.arch.datastore.session.MemorySessionManager
import ru.otus.arch.domain.session.SessionManager
import ru.otus.arch.network.di.networkModule

val di = DI {
    bindSingleton<SessionManager> { MemorySessionManager() }
    import(networkModule)
    import(sharedModule)
}
