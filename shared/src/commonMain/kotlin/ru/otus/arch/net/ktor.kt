package ru.otus.arch.net

import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import ru.otus.arch.Constants
import ru.otus.arch.net.data.Session

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
                    (sessionManager.session.value as? Session.Active.Basic)?.credentials
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
