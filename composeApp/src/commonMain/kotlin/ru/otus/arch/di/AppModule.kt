package ru.otus.arch.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import ru.otus.arch.datastore.di.datastoreModule
import ru.otus.arch.network.di.networkModule

val appModule = DI.Module("app") {
    bindSingleton<CoroutineScope>("app") { MainScope() }
    import(fileSystemModule)
    import(datastoreModule)
    import(networkModule)
    import(sharedModule)
}
