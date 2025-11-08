package ru.otus.arch.server

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.serialization.json.Json
import ru.otus.arch.data.Profile
import ru.otus.arch.data.User
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    private val json = Json

    private val profile = Profile(
        userId = 1,
        name = "Vasya",
        age = 25
    )

    private val user = User(
        userId = profile.userId,
        name = profile.name
    )

    private val newProfileId = 100500

    private val users: Users = mockk{
        every { getUsers() } returns listOf(user)
        every { getProfile(any()) } answers {
            val userId = firstArg<Int>()
            if (userId == profile.userId) {
                profile
            } else {
                throw NotFoundException("User with id $userId not found")
            }
        }
        every { addUser(any()) } answers {
            val newProfile = firstArg<Profile>()
            user.copy(
                userId = newProfileId,
                name = newProfile.name
            )
        }
        every { deleteUser(any()) } answers {
            val userId = firstArg<Int>()
            if (userId == profile.userId) {
                Unit
            } else {
                throw NotFoundException("User with id $userId not found")
            }
        }
    }

    private fun ApplicationTestBuilder.prepareClient(): HttpClient {
        return createClient {
            install(ContentNegotiation) {
                json(Json)
            }
        }
    }

    @Test
    fun usersResponds() = testApplication {
        application {
            module(users)
        }
        val response = prepareClient().get("/users") {
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            listOf(user),
            json.decodeFromString(response.bodyAsText())
        )
    }

    @Test
    fun profileResponds() = testApplication {
        application {
            module(users)
        }
        val response = prepareClient().get("/profiles") {
            url {
                parameters["id"] = profile.userId.toString()
            }
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            profile,
            json.decodeFromString(response.bodyAsText())
        )
    }

    @Test
    fun failsOnProfileNotFound() = testApplication {
        application {
            module(users)
        }
        val response = prepareClient().get("/profiles") {
            url {
                parameters["id"] = 100500.toString()
            }
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun addsUser() = testApplication {
        application {
            module(users)
        }
        val response = prepareClient().post("/users") {
            basicAuth(USERNAME, PASSWORD)
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(profile)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        verify { users.addUser(profile) }
        assertEquals(
            user.copy(userId = newProfileId),
            json.decodeFromString(response.bodyAsText())
        )
    }

    @Test
    fun failsToAddIfNotAuthenticated() = testApplication {
        application {
            module(users)
        }
        val response = prepareClient().post("/users") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(profile)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
        verify(exactly = 0) { users.addUser(any()) }
    }

    @Test
    fun deletesUser() = testApplication {
        application {
            module(users)
        }
        val response = prepareClient().delete("/users") {
            url {
                parameters["id"] = profile.userId.toString()
            }
            basicAuth(USERNAME, PASSWORD)
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.NoContent, response.status)
        verify { users.deleteUser(profile.userId) }
    }

    @Test
    fun failsToDeleteOnUserNotFound() = testApplication {
        application {
            module(users)
        }
        val response = prepareClient().delete("/users") {
            url {
                parameters["id"] = 100500.toString()
            }
            basicAuth(USERNAME, PASSWORD)
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun failsToDeleteOnNotAuthenticated() = testApplication {
        application {
            module(users)
        }
        val response = prepareClient().delete("/users") {
            url {
                parameters["id"] = profile.userId.toString()
            }
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
        verify(exactly = 0) { users.deleteUser(profile.userId) }
    }
}