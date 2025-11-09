package ru.otus.arch.domain.files

interface FilePathProvider {
    fun getUserFilePath(name: String): String
}
