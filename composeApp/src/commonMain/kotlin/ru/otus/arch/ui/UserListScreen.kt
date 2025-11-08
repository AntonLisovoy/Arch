package ru.otus.arch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import arch.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    users: List<User>,
    loggedIn: Boolean,
    onGesture: (gesture: AppGesture) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.users)) },
                actions = {
                    IconButton(onClick = { onGesture(if (loggedIn) AppGesture.Logout else AppGesture.Login) }) {
                        Icon(
                            painter = painterResource(if (loggedIn) Res.drawable.ic_logout else Res.drawable.ic_auth),
                            contentDescription = stringResource(if (loggedIn) Res.string.logout else Res.string.login)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onGesture(AppGesture.AddUser) },
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_add),
                    contentDescription = stringResource(Res.string.add_user)
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users, key = { it.userId }) { user ->
                UserItem(
                    user = user,
                    onSelected = { onGesture(AppGesture.UserSelected(user.userId)) }
                )
            }
        }
    }
}

@Composable
fun UserItem(
    user: User,
    onSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelected),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun UserListScreenPreview() {
    val sampleUsers = listOf(
        User(1, "John Doe"),
        User(2, "Jane Smith"),
        User(3, "Peter Jones")
    )
    MaterialTheme {
        Column {
            UserListScreen(users = sampleUsers, loggedIn = true, onGesture = {})
        }
    }
}
