package ru.otus.arch

import androidx.compose.ui.window.ComposeUIViewController
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance
import platform.posix.exit
import ru.otus.arch.di.appModule

fun MainViewController() = ComposeUIViewController {
    val di = DI {
        import(appModule)
    }

    val model = di.direct.instance<Model>()

    App(model) {
        exit(0)
    }
}