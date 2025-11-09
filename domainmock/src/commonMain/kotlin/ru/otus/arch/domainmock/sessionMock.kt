package ru.otus.arch.domainmock

import ru.otus.arch.domain.session.data.Session

val SESSION_BASIC = Session.Active.Basic(
    username = USER_1.name,
    password = "password"
)