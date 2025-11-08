plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "ru.otus.network"
version = "1.0.0"
application {
    mainClass.set("ru.otus.arch.server.ApplicationKt")
    
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.model)
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.status)
    implementation(libs.ktor.server.contentType)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.openApi)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.serialization.contentJson)
    testImplementation(libs.ktor.server.testHost)
    testImplementation(libs.kotlin.testJunit)
    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.contentNegotiation)
    testImplementation(libs.junit)
    testImplementation(libs.mockk.mockk)
}