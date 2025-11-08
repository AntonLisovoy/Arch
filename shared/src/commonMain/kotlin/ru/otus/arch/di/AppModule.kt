package ru.otus.arch.di

import io.ktor.client.*
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import ru.otus.arch.Model
import ru.otus.arch.net.*
import ru.otus.arch.net.usecase.*
import ru.otus.arch.state.di.stateModule

val di = DI {
    bindProvider<Json> { Json { prettyPrint = true } }
    bindSingleton<SessionManager> { SessionManagerImpl() }
    bindProvider<HttpClient> { ktorHttpClient(instance(), instance()) }
    bindProvider<UsersApi> { UsersApiImpl(instance()) }
    bindProvider<LoadUsers> { LoadUsersImpl(instance()) }
    bindProvider<LoadUserProfile> { LoadUserProfileImpl(instance()) }
    bindProvider<AddUser> { AddUserImpl(instance()) }
    bindProvider<DeleteUser> { DeleteUserImpl(instance()) }

    import(stateModule)

    bindProvider { Model(instance()) }
}
