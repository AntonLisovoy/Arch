package ru.otus.gradle.constgenerator

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

/**
 * Default output directory
 */
const val DEFAULT_OUTPUT_DIR = "generated/constGenerator"

/**
 * Default object name to put constants to
 */
const val DEFAULT_OBJ_NAME = "Constants"

/**
 * Plugin extension
 */
abstract class ConstGeneratorExtension @Inject constructor(project: Project) {

    private val objects = project.objects

    /**
     * Constants definition
     */
    abstract val constants: NamedDomainObjectContainer<ConstDefinition>

    /**
     * Constants package
     */
    val pkgName: Property<String> = objects.property(String::class.java).convention("")

    /**
     * Object name to put constants to
     */
    val objName: Property<String> = objects.property(String::class.java).convention(DEFAULT_OBJ_NAME)

    /**
     * If true - the object is internal
     */
    val pkgInternal: Property<Boolean> = objects.property(Boolean::class.java).convention(true)

    /**
     * Root output directory. Added to `commonMain` class-path
     */
    val outputDir: DirectoryProperty = objects.directoryProperty().convention(
        project.layout.buildDirectory.dir(DEFAULT_OUTPUT_DIR)
    )
}