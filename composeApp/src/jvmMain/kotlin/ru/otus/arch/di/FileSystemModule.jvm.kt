package ru.otus.arch.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import ru.otus.arch.AppConstants
import ru.otus.arch.domain.files.FilePathProvider
import java.io.File

actual val fileSystemModule: DI.Module = DI.Module("fileSystem") {
    bindProvider<FilePathProvider> {
        object : FilePathProvider {
            override fun getUserFilePath(name: String): String {
                val parentFolder = File(System.getProperty("user.home"), AppConstants.APP_NAME)
                if (!parentFolder.exists()) {
                    parentFolder.mkdirs()
                }
                return File(parentFolder, name).path
            }
        }
    }
}