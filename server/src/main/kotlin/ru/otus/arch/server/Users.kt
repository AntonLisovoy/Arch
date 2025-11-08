package ru.otus.arch.server

import io.ktor.server.plugins.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.otus.arch.data.Profile
import ru.otus.arch.data.User
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

interface Users {
    fun getUsers(): List<User>
    fun getProfile(userId: Int): Profile
    fun addUser(profile: Profile): User
    fun deleteUser(userId: Int)
}

@OptIn(ExperimentalTime::class)
class UsersImpl(private var profiles: List<Profile>) : Users{
    override fun getUsers(): List<User> = profiles.map { profile ->
        User(
            userId = profile.userId,
            name = profile.name
        )
    }

    override fun getProfile(userId: Int): Profile = profiles.find { it.userId == userId } ?: throw NotFoundException("User with id $userId not found")

    override fun addUser(profile: Profile): User {
        val creationDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val newProfile = profile.copy(
            userId = profiles.maxOfOrNull { it.userId }?.plus(1) ?: 1,
        )
        profiles = profiles + newProfile

        return User(
            userId = newProfile.userId,
            name = newProfile.name
        )
    }

    override fun deleteUser(userId: Int) {
        val profile = profiles.indexOfFirst { it.userId == userId }.takeIf { it >= 0 } ?: throw NotFoundException("User with id $userId not found")
        profiles = profiles.filterIndexed { index, _ -> index != profile }
    }
}