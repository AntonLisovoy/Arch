package ru.otus.gradle.constgenerator

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

const val EXTENSION_NAME = "constGeneratorConfig"
const val TASK_NAME = "constGenerator"

/**
 * Creates `constGenerator` task that creates a constants file
 */
abstract class ConstGeneratorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create(
            EXTENSION_NAME,
            ConstGeneratorExtension::class.java,
            project
        )

        // Add a task that uses configuration from the extension object
        val constGenerator = project.tasks.register(TASK_NAME, ConstGeneratorTask::class.java) {
            extension.constants.all {  }
            it.constants.set(extension.constants.asMap)
            it.pkgName.set(extension.pkgName)
            it.objName.set(extension.objName)
            it.pkgInternal.set(extension.pkgInternal)
            it.outputDir.set(extension.outputDir)
        }

        project.extensions.findByType(KotlinMultiplatformExtension::class.java)?.run {
            sourceSets
                .getByName(KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME)
                .kotlin
                .srcDir(constGenerator)

            targets.all { t ->
                t.compilations.all { c ->
                    c.compileTaskProvider.configure { it.dependsOn(constGenerator) }
                }
            }
        }
    }
}