dependencies {
    compileOnly("com.gmail.nossr50.mcMMO:mcMMO:2.1.200") // https://github.com/mcMMO-Dev/mcMMO/tree/2c849d9cb435712e3f560350b46bfeb7086d174a
    compileOnly("org.spigotmc:spigot-api:1.15.2-R0.1-SNAPSHOT")
}

tasks {
    compileKotlin { kotlinOptions.jvmTarget = "1.8" }
    compileTestKotlin { kotlinOptions.jvmTarget = "1.8" }

    shadowJar {
        relocate("kotlin", "xyz.acrylicstyle.antigooglemaps.libs.kotlin")

        minimize()
    }

    withType<org.gradle.jvm.tasks.Jar> {
        archiveFileName.set("AntiGoogleMaps-${parent?.version}.jar")
    }
}
