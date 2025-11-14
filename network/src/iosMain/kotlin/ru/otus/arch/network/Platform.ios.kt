package ru.otus.arch.network

import platform.UIKit.UIDevice

internal class IOSPlatform: Platform, CommonPlatform by CommonPlatform.Impl {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val host: String = "127.0.0.1"
}

internal actual fun getPlatform(): Platform = IOSPlatform()
