package ru.otus.arch

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.kodein.di.direct
import org.kodein.di.instance
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState
import ru.otus.arch.di.di
import ru.otus.arch.ui.*

@Composable
@Preview
fun App(onTerminated: () -> Unit = { }) {
    val model = remember {
        di.direct.instance<Model>()
    }

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