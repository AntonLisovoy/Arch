package ru.otus.arch.state.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.factory
import org.kodein.di.instance
import ru.otus.arch.domain.net.usecase.AddUser
import ru.otus.arch.domain.net.usecase.DeleteUser
import ru.otus.arch.domain.net.usecase.LoadUserProfile
import ru.otus.arch.domain.net.usecase.LoadUsers
import ru.otus.arch.state.AddingUserState
import ru.otus.arch.state.AppStateFactory
import ru.otus.arch.state.AppStateFactoryImpl
import ru.otus.arch.state.AuthProxy
import ru.otus.arch.state.DeletingUserState
import ru.otus.arch.state.UserListState
import ru.otus.arch.state.UserProfileState

val stateModule = DI.Module(name = "StateMachine") {
    bindProvider { UserListState.Factory(instance<LoadUsers>(), instance()) }
    bindProvider { UserProfileState.Factory(instance<LoadUserProfile>()) }
    bindProvider { AuthProxy.Factory(factory()) }
    bindProvider { AddingUserState.Factory(instance<AddUser>()) }
    bindProvider { DeletingUserState.Factory(instance<DeleteUser>()) }
    bindProvider<AppStateFactory> { AppStateFactoryImpl(instance(), instance(), instance(), instance(), instance()) }
}