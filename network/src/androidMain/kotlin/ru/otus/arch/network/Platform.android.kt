package ru.otus.arch.network

import android.os.Build

internal class AndroidPlatform : Platform, CommonPlatform by CommonPlatform.Impl {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val host: String = "10.0.2.2"
}

internal actual fun getPlatform(): Platform = AndroidPlatform()
