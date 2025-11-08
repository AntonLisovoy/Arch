package ru.otus.arch.state

import io.ktor.client.plugins.auth.providers.*
import kotlinx.coroutines.launch
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState
import ru.otus.arch.net.SessionManager
import ru.otus.arch.net.data.Session
import kotlin.properties.Delegates

internal class BasicLoginState(
    context: AppContext,
    private val error: Throwable?,
    private val onLogin: AppStateFactory.() -> AppState,
    private val onCancel: AppStateFactory.() -> AppState,
    private val sessionManager: SessionManager
) : BaseAppState(context) {

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

    override fun doProcess(gesture: AppGesture) {
        when(gesture) {
            is AppGesture.UsernameChanged -> updateData {
                copy(username = gesture.username)
            }
            is AppGesture.PasswordChanged -> updateData {
                copy(password = gesture.password)
            }
            AppGesture.Action -> login()
            AppGesture.Back -> setMachineState(factory.onCancel())
            else -> super.doProcess(gesture)
        }
    }

    private fun LoginData.isValid(): Boolean = username.isNotBlank() && password.isNotBlank()

    private fun login() = stateScope.launch {
        sessionManager.login(Session.Active.Basic(BasicAuthCredentials(loginData.username, loginData.password)))
        setMachineState(factory.onLogin())
    }

    private fun render(loginData: LoginData) {
        setUiState(
            AppUiState.Login(
                username = loginData.username,
                password = loginData.password,
                isEnabled = loginData.isValid(),
                error = error
            )
        )
    }

    class Factory(private val sessionManager: SessionManager) {
        operator fun invoke(
            context: AppContext,
            error: Throwable?,
            onLogin: AppStateFactory.() -> AppState,
            onCancel: AppStateFactory.() -> AppState,
        ) = BasicLoginState(
            context,
            error,
            onLogin,
            onCancel,
            sessionManager
        )
    }
}