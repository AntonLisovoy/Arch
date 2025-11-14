package ru.otus.arch.domain.net.usecase

interface DeleteUser {
    suspend operator fun invoke(id: Int)
}
