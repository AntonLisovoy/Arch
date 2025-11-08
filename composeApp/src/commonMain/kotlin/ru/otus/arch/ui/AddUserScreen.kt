package ru.otus.arch.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import arch.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(
    state: AppUiState.AddUserForm,
    onGesture: (AppGesture) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.add_user)) },
                navigationIcon = {
                    IconButton(onClick = { onGesture(AppGesture.Back) }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_back),
                            contentDescription = stringResource(Res.string.back)
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { onGesture(AppGesture.AddUserForm.NameChanged(it)) },
                label = { Text(stringResource(Res.string.name)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.age.takeIf { it > 0 }?.toString().orEmpty(),
                onValueChange = {
                    it.toIntOrNull()?.let { age ->
                        onGesture(AppGesture.AddUserForm.AgeChanged(age))
                    }
                },
                label = { Text(stringResource(Res.string.age)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.interests,
                onValueChange = {
                    onGesture(AppGesture.AddUserForm.InterestsChanged(it))
                },
                label = { Text(stringResource(Res.string.interests)) },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { onGesture(AppGesture.Action) },
                enabled = state.addEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(Res.string.add))
            }
        }
    }
}
