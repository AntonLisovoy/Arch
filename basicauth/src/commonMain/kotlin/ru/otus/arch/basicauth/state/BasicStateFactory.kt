package ru.otus.arch.basicauth.state

import ru.otus.arch.basicauth.api.BasicAuthApi
import ru.otus.arch.basicauth.api.BasicAuthFlowHost

internal interface BasicStateFactory : BasicAuthApi {
    fun forgotPassword(error: Throwable?): BasicAuthState

    class Impl(
        flowHost: BasicAuthFlowHost,
        private val createLogin: LoginState.Factory,
        private val createForgotPassword: ForgotPasswordState.Factory
    ) : BasicStateFactory {

        private val context = object : BasicAuthContext {
            override val factory = this@Impl
            override val flowHost = flowHost
        }

        override fun start(error: Throwable?) = createLogin(context, error)

        override fun forgotPassword(error: Throwable?) = createForgotPassword(context, error)
    }
}
