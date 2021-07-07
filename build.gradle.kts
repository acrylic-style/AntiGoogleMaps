plugins {
    kotlin("jvm") version "1.5.20"
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

group = "xyz.acrylicstyle"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://libraries.minecraft.net/") }
}

subprojects {
    apply {
        plugin("java")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("com.github.johnrengelman.shadow")
    }

    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://libraries.minecraft.net/") }
    }

    dependencies {
        implementation(kotlin("stdlib"))
        compileOnly("com.mojang:brigadier:1.0.18")
    }
}
