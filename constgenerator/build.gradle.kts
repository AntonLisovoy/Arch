plugins {
    alias(libs.plugins.kotlinJvm)
    id("java-gradle-plugin")
}

group = "ru.otus.gradle"
version = "1.0.0"
val pluginId = "${group}.constgenerator"

dependencies {
    compileOnly(gradleApi())
    compileOnly(kotlin("gradle-plugin"))
    implementation(libs.kotlin.poet)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

gradlePlugin {
    plugins {
        create(pluginId) {
            id = pluginId
            implementationClass = "ru.otus.gradle.constgenerator.ConstGeneratorPlugin"
            version = version
            displayName = "Generates Kotlin constants"
        }
    }
}
