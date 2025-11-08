package ru.otus.arch

class JVMPlatform: Platform, CommonPlatform by CommonPlatform.Impl {
    override val name: String = "Java Desktop: ${System.getProperty("java.version")}"
    override val host: String = "127.0.0.1"
}

actual fun getPlatform(): Platform = JVMPlatform()