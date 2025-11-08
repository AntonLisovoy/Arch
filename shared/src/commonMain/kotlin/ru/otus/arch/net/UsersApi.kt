package ru.otus.arch.net

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.otus.arch.data.Profile
import ru.otus.arch.data.User
import ru.otus.arch.getPlatform

internal interface UsersApi {
    suspend fun loadUsers(): List<User>
    suspend fun loadUserProfile(id: Int): Profile
    suspend fun addUser(profile: Profile): User
    suspend fun deleteUser(id: Int)
}

internal class UsersApiImpl(private val httpClient: HttpClient) : UsersApi {
    override suspend fun loadUsers(): List<User> {
        val result = httpClient.get {
            platformUrl(listOf("users"))
            contentType(ContentType.Application.Json)
        }
        return result.body()
    }

    override suspend fun loadUserProfile(id: Int): Profile {
        val result = httpClient.get {
            platformUrl(listOf("profiles")) {
                parameters["id"] = id.toString()
            }
            contentType(ContentType.Application.Json)
        }
        return result.body()
    }

    override suspend fun addUser(profile: Profile): User {
        val result = httpClient.post {
            platformUrl(listOf("users")) {
                setBody(profile)
            }
            contentType(ContentType.Application.Json)
        }
        return result.body()
    }

    override suspend fun deleteUser(id: Int) {
        httpClient.delete {
            platformUrl(listOf("users")) {
                parameters["id"] = id.toString()
            }
        }
    }

    companion object {
        private fun HttpRequestBuilder.platformUrl(endpoint: List<String>, extraUrl: URLBuilder.() -> Unit = {}) {
            url {
                host = getPlatform().host
                port = getPlatform().port
                protocol  = URLProtocol.HTTP
                appendPathSegments(endpoint)
                extraUrl()
            }
        }
    }
}