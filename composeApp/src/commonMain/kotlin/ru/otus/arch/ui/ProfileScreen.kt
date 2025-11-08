package ru.otus.arch.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import arch.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.Profile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(profile: Profile, onGesture: (AppGesture) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(profile.name)
                },
                navigationIcon = {
                    IconButton(onClick = { onGesture(AppGesture.Back) }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_back),
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onGesture(AppGesture.DeleteUser) }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_delete),
                            contentDescription = stringResource(Res.string.delete_user)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(AppDimens.marginHorizontal),
            contentAlignment = Alignment.Center
        ) {
            ProfileContent(profile)
        }
    }
}

@Composable
private fun ProfileContent(profile: Profile) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = profile.name,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppDimens.marginVerticalSmall)
        )
        Text(
            text = "Age: ${profile.age}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = AppDimens.marginVerticalSmall)
        )
        if (profile.interests.isNotEmpty()) {
            Text(
                text = "Interests: ${profile.interests.joinToString()}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = AppDimens.marginVerticalSmall)
            )
        }
    }
}
