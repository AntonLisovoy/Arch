package ru.otus.arch.network

internal class WebPlatform: Platform, CommonPlatform by CommonPlatform.Impl {
    override val name: String = "Web"
    override val host: String = "localhost"
}

internal actual fun getPlatform(): Platform = WebPlatform()
