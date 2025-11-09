package ru.otus.arch

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance
import ru.otus.arch.di.appModule

fun main() = application {
    val di = DI {
        import(appModule)
    }

    val model = di.direct.instance<Model>()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Network",
    ) {
        App(model) {
            exitApplication()
        }
    }
}