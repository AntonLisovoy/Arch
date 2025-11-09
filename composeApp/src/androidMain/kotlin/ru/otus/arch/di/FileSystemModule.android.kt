package ru.otus.arch.di

import android.content.Context
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import ru.otus.arch.domain.files.FilePathProvider

actual val fileSystemModule: DI.Module = DI.Module("fileSystem") {
    bindProvider<FilePathProvider> {
        object : FilePathProvider {
            override fun getUserFilePath(name: String): String {
                val context = instance<Context>()
                return context.filesDir.resolve(name).absolutePath
            }
        }
    }
}
