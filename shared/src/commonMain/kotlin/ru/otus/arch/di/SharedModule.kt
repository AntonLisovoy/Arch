package ru.otus.arch.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import ru.otus.arch.Model
import ru.otus.arch.basicauth.di.basicAuthModule
import ru.otus.arch.state.di.stateModule

val sharedModule = DI.Module("shared") {
    import(basicAuthModule)
    import(stateModule)

    bindProvider { Model(instance()) }
}
