import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    compilerOptions.optIn.add("kotlin.time.ExperimentalTime")
    compilerOptions.optIn.add("kotlin.js.ExperimentalJsExport")

    js {
        browser()
        binaries.library()
        generateTypeScriptDefinitions()
        browser {
            @OptIn(ExperimentalDistributionDsl::class)
            distribution {
                outputDirectory.set(file("$projectDir/output/npm"))
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain)
            implementation(projects.shared)
            implementation(projects.memory)
            implementation(libs.kotlinx.coroutinesCore)
            implementation(libs.kodein)
        }
    }
}
