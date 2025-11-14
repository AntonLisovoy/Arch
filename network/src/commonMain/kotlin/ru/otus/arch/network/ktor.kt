package ru.otus.arch.network

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.otus.arch.Constants
import ru.otus.arch.domain.session.SessionManager
import ru.otus.arch.domain.session.data.Session

fun ktorHttpClient(json: Json, sessionManager: SessionManager): HttpClient {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
        }

        install(Auth) {
            basic {
                credentials {
                    (sessionManager.session.value as? Session.Active.Basic)?.let {
                        BasicAuthCredentials(it.username, it.password)
                    }
                }
                realm = Constants.Auth.REALM_ADMIN

                sendWithoutRequest { true }
            }
        }

        expectSuccess = true
    }

    client.plugin(HttpSend).intercept { request: HttpRequestBuilder ->
        Napier.i { "Requesting: ${ request.url }" }
        execute(request)
    }

    return client
}
