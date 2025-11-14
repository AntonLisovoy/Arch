package ru.otus.arch.basicauth.di

import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindProvider
import org.kodein.di.instance
import ru.otus.arch.basicauth.api.BasicAuthApi
import ru.otus.arch.basicauth.api.BasicAuthFlowHost
import ru.otus.arch.basicauth.state.BasicStateFactory
import ru.otus.arch.basicauth.state.ForgotPasswordState
import ru.otus.arch.basicauth.state.LoginState

val basicAuthModule = DI.Module("basicAuth") {
    bindProvider { LoginState.Factory(instance()) }
    bindProvider { ForgotPasswordState.Factory() }
    bindFactory<BasicAuthFlowHost, BasicAuthApi> { flowHost: BasicAuthFlowHost ->
        BasicStateFactory.Impl(flowHost, instance(), instance())
    }
}