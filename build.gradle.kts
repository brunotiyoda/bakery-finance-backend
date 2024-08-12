val kotlin_version: String by project
val logback_version: String by project
val awssdk_version: String by project
val exposed_version: String by project
val h2_version: String by project
val koin_version: String by project
val postgres_version: String by project
val postgres_driver_version: String by project
val bcrypt_version: String by project
val mockk_version: String by project

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.12"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
}

group = "example.com"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-host-common-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-server-rate-limit")

    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")

    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("com.impossibl.pgjdbc-ng:pgjdbc-ng:$postgres_driver_version")

    implementation("software.amazon.awssdk:auth:$awssdk_version")
    implementation("software.amazon.awssdk:core:$awssdk_version")
    implementation("software.amazon.awssdk:textract:$awssdk_version")

    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-json:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-money:$exposed_version")
    implementation("com.h2database:h2:$h2_version")

    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-core:$koin_version")

    implementation("at.favre.lib:bcrypt:$bcrypt_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")

    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.mockk:mockk:$mockk_version")

}
