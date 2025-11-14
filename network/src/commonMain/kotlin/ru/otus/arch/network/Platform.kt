package ru.otus.arch.network

internal interface Platform : CommonPlatform {
    val name: String
    val host: String
}

internal interface CommonPlatform {
    val port: Int

    object Impl : CommonPlatform {
        override val port: Int = 8080
    }
}

internal expect fun getPlatform(): Platform
