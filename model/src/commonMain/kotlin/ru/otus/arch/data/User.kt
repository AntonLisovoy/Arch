package ru.otus.arch.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class User(
    @SerialName("id")
    val userId: Int,
    val name: String
)