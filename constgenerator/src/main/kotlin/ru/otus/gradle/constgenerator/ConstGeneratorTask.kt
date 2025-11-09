package ru.otus.gradle.constgenerator

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * Creates constants
 */
@CacheableTask
abstract class ConstGeneratorTask : DefaultTask() {

    init {
        description = "Creates constants given the input map"
        group = "build"
    }

    /**
     * Constants definition
     */
    @get:Input
    abstract val constants: MapProperty<String, ConstDefinition>

    /**
     * Constants package
     */
    @get:Input
    abstract val pkgName: Property<String>

    /**
     * Object name to put constants to
     */
    @get:Input
    abstract val objName: Property<String>

    /**
     * If true - the object is internal
     */
    @get:Input
    abstract val pkgInternal: Property<Boolean>

    /**
     * Root output directory. Added to `commonMain` class-path
     */
    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generateConstants() {
        val pkg = pkgName.getOrElse("")
        val obj = objName.get()

        val props = constants.get().map { (key, value) ->
            val (type, codeBlock) = value.type.get()
            PropertySpec.builder(key, type, KModifier.CONST).initializer(codeBlock(value.value.get())).build()
        }

        var constSpec = TypeSpec.objectBuilder(objName.get()).addProperties(props)
        if (pkgInternal.get()) {
            constSpec = constSpec.addModifiers(KModifier.INTERNAL)
        }

        val fileSpec = FileSpec.builder(pkg, obj)
            .addType(constSpec.build())
            .build()

        fileSpec.writeTo(outputDir.get().asFile)
    }
}