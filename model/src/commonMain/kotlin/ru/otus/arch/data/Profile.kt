package ru.otus.arch.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class Profile(
    @SerialName("id")
    val userId: Int,
    val name: String,
    val age: Int,
    val interests: Set<String> = emptySet()
) {
    companion object {
        val EMPTY = Profile(
            userId = 0,
            name = "",
            age = 0,
            interests = emptySet()
        )
    }
}