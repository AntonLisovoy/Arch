package ru.otus.arch.network.di

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import ru.otus.arch.domain.net.usecase.AddUser
import ru.otus.arch.domain.net.usecase.DeleteUser
import ru.otus.arch.domain.net.usecase.LoadUserProfile
import ru.otus.arch.domain.net.usecase.LoadUsers
import ru.otus.arch.network.api.UsersApi
import ru.otus.arch.network.api.UsersApiImpl
import ru.otus.arch.network.ktorHttpClient
import ru.otus.arch.network.usecase.AddUserImpl
import ru.otus.arch.network.usecase.DeleteUserImpl
import ru.otus.arch.network.usecase.LoadUserProfileImpl
import ru.otus.arch.network.usecase.LoadUsersImpl

val networkModule = DI.Module("network") {
    bindProvider<Json> { Json { prettyPrint = true } }
    bindProvider<HttpClient> { ktorHttpClient(instance(), instance()) }
    bindProvider<UsersApi> { UsersApiImpl(instance()) }
    bindProvider<LoadUsers> { LoadUsersImpl(instance()) }
    bindProvider<LoadUserProfile> { LoadUserProfileImpl(instance()) }
    bindProvider<AddUser> { AddUserImpl(instance()) }
    bindProvider<DeleteUser> { DeleteUserImpl(instance()) }
}
