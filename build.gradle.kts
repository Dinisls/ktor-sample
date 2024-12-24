plugins {
    kotlin("jvm") version "1.9.10"
    id("io.ktor.plugin") version "2.3.4"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.3.4")
    implementation("io.ktor:ktor-server-netty:2.3.4")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.4")
    implementation("io.ktor:ktor-server-html-builder:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.9.1")
    implementation("ch.qos.logback:logback-classic:1.4.11") // Adicionando logback-classic
    testImplementation("io.ktor:ktor-server-tests:2.3.4")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.10")
    implementation("io.ktor:ktor-server-core:2.3.0")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.0")
    implementation("io.ktor:ktor-server-call-logging:2.3.0")
    implementation("io.ktor:ktor-server-status-pages:2.3.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("io.ktor:ktor-server-core:2.3.0") // Certifique-se de usar a versão compatível
    implementation("io.ktor:ktor-server-netty:2.3.0")// Para logs
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(20))
    }
}