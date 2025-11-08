package ru.otus.arch.state

import ru.otus.arch.data.Profile
import ru.otus.arch.data.User

val PROFILE_1 = Profile(
    userId = 1,
    name = "Vasya",
    age = 25,
    interests = setOf("fishing", "coroutines", "soccer")
)

val USER_1 = User(
    userId = PROFILE_1.userId,
    name = PROFILE_1.name
)

val PROFILE_2 = Profile(
    userId = 2,
    name = "Masha",
    age = 30,
    interests = setOf("hiking", "cooking", "chess")
)

val USER_2 = User(
    userId = PROFILE_2.userId,
    name = PROFILE_2.name
)
