package ru.otus.arch.server

import com.motorro.cookbook.server.Auth
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import ru.otus.arch.data.Profile
import ru.otus.arch.Constants as CommonConstants


fun main() {
    embeddedServer(
        Netty,
        host = SERVER_HOST,
        port = SERVER_PORT,
        module = {
            install(CORS) {
                anyHost()
                anyMethod()
                allowHeader(HttpHeaders.ContentType)
                allowHeader(HttpHeaders.Authorization)
            }
            module(UsersImpl(profiles))
        }
    ).start(wait = true)
}

fun Application.module(users: Users, auth: Auth = Auth.Impl()) {

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
        })
    }

    install(Authentication) {
        auth.auth(this, CommonConstants.Auth.REALM_ADMIN)
    }

    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.message ?: "Bad request")
        }
        exception<IllegalStateException> { call, cause ->
            call.respond(HttpStatusCode.Conflict, cause.message ?: "Conflict")
        }
        status(HttpStatusCode.Unauthorized) { call, _ ->
            call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
        }
        status(HttpStatusCode.Forbidden) { call, _ ->
            call.respond(HttpStatusCode.Forbidden, "Forbidden")
        }
        exception<NotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, cause.message ?: "Not found")
        }
        unhandled { call ->
            call.respond(HttpStatusCode.NotFound, "Not found")
        }
    }
    routing {
        openAPI(path="openapi", swaggerFile = "openapi/documentation.yaml")

        get("/users") {
            call.respond(users.getUsers())
        }

        get("/profiles") {
            call.respond(users.getProfile(call.parameters["id"]?.toIntOrNull() ?: throw IllegalArgumentException("Invalid user id")))
        }

        authenticate(CommonConstants.Auth.REALM_ADMIN) {
            post("/users") {
                call.respond(users.addUser(call.receive<Profile>()))
            }
            delete("users") {
                users.deleteUser(call.parameters["id"]?.toIntOrNull() ?: throw IllegalArgumentException("Invalid user id"))
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}