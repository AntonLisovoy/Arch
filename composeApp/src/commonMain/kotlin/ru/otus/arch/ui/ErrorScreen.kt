package ru.otus.arch.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import arch.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.otus.arch.data.AppUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorScreen(error: AppUiState.Error, onDismiss: () -> Unit, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.error)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(AppDimens.marginHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(Res.string.error),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = AppDimens.marginVertical)
            )
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error.error.message ?: "Unknown error",
                    modifier = Modifier.padding(AppDimens.marginHorizontal)
                )
            }
            Button(
                onClick = onDismiss,
                modifier = Modifier.padding(top = AppDimens.marginVertical)
            ) {
                val buttonText = if (error.canRetry) {
                    stringResource(Res.string.retry)
                } else {
                    stringResource(Res.string.close)
                }
                Text(buttonText)
            }
        }
    }
}

@Preview
@Composable
private fun ErrorScreenCanRetryPreview() {
    MaterialTheme {
        ErrorScreen(
            error = AppUiState.Error(RuntimeException("Failed to load data"), canRetry = true),
            onDismiss = {},
            onBack = {}
        )
    }
}

@Preview
@Composable
private fun ErrorScreenCannotRetryPreview() {
    MaterialTheme {
        ErrorScreen(
            error = AppUiState.Error(RuntimeException("Session expired"), canRetry = false),
            onDismiss = {},
            onBack = {}
        )
    }
}
