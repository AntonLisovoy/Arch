package ru.otus.arch.domain.session.data

import kotlinx.serialization.Serializable

@Serializable
sealed class Session {
    @Serializable
    data object NONE : Session()

    @Serializable
    sealed class Active : Session() {
        abstract val username: String

        @Serializable
        data class Basic(override val username: String, val password: String) : Active()
    }
}