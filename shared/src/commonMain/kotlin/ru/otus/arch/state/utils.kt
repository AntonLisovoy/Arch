package ru.otus.arch.state

import io.ktor.client.plugins.*
import io.ktor.http.*

fun Throwable.isFatal(): Boolean = when {
    this.isIoException() -> false
    else -> true
}

expect fun Throwable.isIoException(): Boolean

fun Throwable.isUnauthorized(): Boolean = when {
    this is ClientRequestException && HttpStatusCode.Unauthorized == response.status -> true
    else -> false
}