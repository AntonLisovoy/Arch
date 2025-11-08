package ru.otus.arch.state.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import ru.otus.arch.state.*

val stateModule = DI.Module(name = "StateMachine") {
    bindProvider { UserListState.Factory(instance(), instance()) }
    bindProvider { UserProfileState.Factory(instance()) }
    bindProvider { BasicLoginState.Factory(instance()) }
    bindProvider { AddingUserState.Factory(instance()) }
    bindProvider { DeletingUserState.Factory(instance()) }
    bindProvider<AppStateFactory> { AppStateFactoryImpl(instance(), instance(), instance(), instance(), instance()) }
}