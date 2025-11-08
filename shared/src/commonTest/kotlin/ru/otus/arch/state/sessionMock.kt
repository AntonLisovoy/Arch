package ru.otus.arch.state

import io.ktor.client.plugins.auth.providers.*
import ru.otus.arch.net.data.Session

val SESSION_BASIC = Session.Active.Basic(BasicAuthCredentials(
    username = USER_1.name,
    password = "password"
))