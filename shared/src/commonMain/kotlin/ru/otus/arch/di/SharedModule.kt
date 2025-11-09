package ru.otus.arch.di

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import ru.otus.arch.Model
import ru.otus.arch.net.UsersApi
import ru.otus.arch.net.UsersApiImpl
import ru.otus.arch.net.ktorHttpClient
import ru.otus.arch.net.usecase.AddUser
import ru.otus.arch.net.usecase.AddUserImpl
import ru.otus.arch.net.usecase.DeleteUser
import ru.otus.arch.net.usecase.DeleteUserImpl
import ru.otus.arch.net.usecase.LoadUserProfile
import ru.otus.arch.net.usecase.LoadUserProfileImpl
import ru.otus.arch.net.usecase.LoadUsers
import ru.otus.arch.net.usecase.LoadUsersImpl
import ru.otus.arch.state.di.stateModule

val sharedModule = DI.Module("shared") {
    bindProvider<Json> { Json { prettyPrint = true } }
    bindProvider<HttpClient> { ktorHttpClient(instance(), instance()) }
    bindProvider<UsersApi> { UsersApiImpl(instance()) }
    bindProvider<LoadUsers> { LoadUsersImpl(instance()) }
    bindProvider<LoadUserProfile> { LoadUserProfileImpl(instance()) }
    bindProvider<AddUser> { AddUserImpl(instance()) }
    bindProvider<DeleteUser> { DeleteUserImpl(instance()) }

    import(stateModule)

    bindProvider { Model(instance()) }
}
