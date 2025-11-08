package ru.otus.arch.state

import java.io.IOException

actual fun Throwable.isIoException(): Boolean {
    return this is IOException
}