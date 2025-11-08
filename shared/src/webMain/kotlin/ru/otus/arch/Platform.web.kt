package ru.otus.arch


class WebPlatform: Platform, CommonPlatform by CommonPlatform.Impl {
    override val name: String = "Web"
    override val host: String = "localhost"
}

actual fun getPlatform(): Platform = WebPlatform()