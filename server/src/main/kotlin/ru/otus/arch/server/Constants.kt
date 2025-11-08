package ru.otus.arch.server

import ru.otus.arch.data.Profile


const val SERVER_HOST = "0.0.0.0"
const val SERVER_PORT = 8080

const val USERNAME = "admin"
const val PASSWORD = "password"

val profiles = listOf(
    Profile(
        userId = 1,
        name = "Vasya",
        age = 25,
        interests = setOf("fishing", "coroutines", "soccer")
    ),
    Profile(
        userId = 2,
        name = "Masha",
        age = 30,
        interests = setOf("hiking", "cooking", "chess")
    )
)