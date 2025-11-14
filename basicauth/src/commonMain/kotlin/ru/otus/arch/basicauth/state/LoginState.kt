package ru.otus.arch.basicauth.state

import kotlinx.coroutines.launch
import ru.otus.arch.basicauth.data.BasicAuthGesture
import ru.otus.arch.basicauth.data.BasicAuthUiState
import ru.otus.arch.domain.session.SessionManager
import ru.otus.arch.domain.session.data.Session
import kotlin.properties.Delegates

internal class LoginState(
    context: BasicAuthContext,
    private val error: Throwable?,
    private val sessionManager: SessionManager
) : BasicAuthState(context) {

    private data class LoginData(val username: String, val password: String)

    private var loginData by Delegates.observable(sessionManager.session.value.let { LoginData((it as? Session.Active)?.username.orEmpty(), "") }) { _, _, newValue ->
        render(newValue)
    }

    private inline fun updateData(block: LoginData.() -> LoginData) {
        loginData = loginData.block()
    }

    override fun doStart() {
        render(loginData)
    }

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun doProcess(gesture: BasicAuthGesture) {
        when(gesture) {
            is BasicAuthGesture.UsernameChanged -> updateData {
                copy(username = gesture.username)
            }
            is BasicAuthGesture.PasswordChanged -> updateData {
                copy(password = gesture.password)
            }
            BasicAuthGesture.Action -> {
                login()
            }
            BasicAuthGesture.Back -> {
                flowHost.onCancel()
            }
            BasicAuthGesture.ForgotPassword -> {
                setMachineState(factory.forgotPassword(error))
            }
            else -> super.doProcess(gesture)
        }
    }

    private fun LoginData.isValid(): Boolean = username.isNotBlank() && password.isNotBlank()

    private fun login() {
        if (loginData.isValid().not()) return
        stateScope.launch {
            sessionManager.login(loginData.username, loginData.password)
            flowHost.onLogin()
        }
    }

    private fun render(loginData: LoginData) {
        setUiState(
            BasicAuthUiState.Login(
                username = loginData.username,
                password = loginData.password,
                isEnabled = loginData.isValid(),
                error = error
            )
        )
    }

    class Factory(private val sessionManager: SessionManager) {
        operator fun invoke(
            context: BasicAuthContext,
            error: Throwable?,
        ) = LoginState(
            context,
            error,
            sessionManager
        )
    }
}