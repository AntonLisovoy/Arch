package ru.otus.arch.state

import kotlinx.io.IOException

actual fun Throwable.isIoException(): Boolean {
    return this is IOException
}