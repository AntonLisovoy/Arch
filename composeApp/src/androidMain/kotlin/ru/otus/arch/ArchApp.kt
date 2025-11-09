package ru.otus.arch

import android.app.Application
import android.content.Context
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bindProvider
import org.kodein.di.delegate

class ArchApp : Application(), DIAware {
    override val di: DI by DI.lazy {
        bindProvider<Application> { this@ArchApp }
        delegate<Context>().to<Application>()
    }
}