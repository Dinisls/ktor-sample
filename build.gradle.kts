plugins {
    application
    kotlin("jvm") version "1.9.20" // Substitua pela versão desejada do Kotlin
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.3.0") // Verifique a versão mais recente
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0") // Para serialização JSON
    implementation("io.ktor:ktor-server-content-negotiation:2.3.0") // Para Content Negotiation
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("io.ktor:ktor-server-netty:2.3.0")  // Versão do Ktor (2.x)
    implementation("io.ktor:ktor-html-builder:2.3.0") // Versão do Ktor HTML Builder compatível com Ktor 2.x
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
    testImplementation("io.ktor:ktor-server-tests:2.3.0") // Para testes

}

application {
    mainClass.set("com.example.ApplicationKt") // Altere para o seu pacote principal
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "21" // Ajuste para a versão desejada do JVM target
    }
}
