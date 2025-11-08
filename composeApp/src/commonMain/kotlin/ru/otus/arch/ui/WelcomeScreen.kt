package ru.otus.arch.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import arch.composeapp.generated.resources.Res
import arch.composeapp.generated.resources.next
import arch.composeapp.generated.resources.welcome
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.otus.arch.data.AppGesture

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(onGesture: (AppGesture) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.welcome)) },
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
                text = stringResource(Res.string.welcome),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = AppDimens.marginVertical)
            )
            Button(
                onClick = { onGesture(AppGesture.Action) },
                modifier = Modifier.padding(top = AppDimens.marginVertical)
            ) {
                Text(stringResource(Res.string.next))
            }
        }
    }
}

@Preview
@Composable
private fun WelcomeScreenPreview() {
    MaterialTheme {
        WelcomeScreen {  }
    }
}
