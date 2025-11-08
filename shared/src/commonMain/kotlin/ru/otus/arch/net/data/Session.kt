package ru.otus.arch.net.data

import io.ktor.client.plugins.auth.providers.*

sealed class Session {
    data object NONE : Session()

    sealed class Active : Session() {
        abstract val username: String

        data class Basic(val credentials: BasicAuthCredentials) : Active() {
            override val username: String = credentials.username
        }
    }
}