package ru.otus.arch.ui

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandler(onBack: () -> Unit)