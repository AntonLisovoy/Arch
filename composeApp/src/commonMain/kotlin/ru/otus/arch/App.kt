package ru.otus.arch

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState
import ru.otus.arch.ui.AddUserScreen
import ru.otus.arch.ui.BackHandler
import ru.otus.arch.ui.ErrorScreen
import ru.otus.arch.ui.LoadingScreen
import ru.otus.arch.ui.LoginScreen
import ru.otus.arch.ui.ProfileScreen
import ru.otus.arch.ui.UserListScreen
import ru.otus.arch.ui.WelcomeScreen

@Composable
@Preview
fun App(model: Model, onTerminated: () -> Unit = { }) {
    val onBack = { model.process(AppGesture.Back) }

    MaterialTheme {
        BackHandler(onBack)

        when(val state = model.uiState.collectAsStateWithLifecycle().value) {
            AppUiState.Welcome -> WelcomeScreen(model::process)
            AppUiState.Loading -> LoadingScreen()
            is AppUiState.UserList -> UserListScreen(
                users = state.users,
                loggedIn = state.loggedIn,
                onGesture = model::process
            )
            is AppUiState.UserProfile -> ProfileScreen(
                profile = state.profile,
                onGesture = model::process
            )
            is AppUiState.Error -> ErrorScreen(
                error = state,
                onDismiss = { model.process(AppGesture.Action) },
                onBack = onBack
            )
            is AppUiState.Login -> LoginScreen(
                state = state,
                onGesture = model::process
            )
            is AppUiState.AddUserForm -> AddUserScreen(
                state = state,
                onGesture = model::process
            )
            AppUiState.Terminated -> LaunchedEffect(state) {
                onTerminated()
            }
        }
    }
}