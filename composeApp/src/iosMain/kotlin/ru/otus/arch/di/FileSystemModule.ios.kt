package ru.otus.arch.di

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.io.files.Path
import org.kodein.di.DI
import org.kodein.di.bindProvider
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import ru.otus.arch.domain.files.FilePathProvider

actual val fileSystemModule: DI.Module = DI.Module("fileSystem") {
    bindProvider<FilePathProvider> {
        object : FilePathProvider {
            @OptIn(ExperimentalForeignApi::class)
            override fun getUserFilePath(name: String): String {
                val parentFolder: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null,
                )

                val parentFolderPath = requireNotNull(parentFolder?.path) {
                    "Directory not created"
                }

                return Path(parentFolderPath, name).toString()
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun fileDirectory(): String {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path) {
        "Directory not created"
    }
}