import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
    java
    `java-library`
    `maven-publish`

    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.1.1"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "org.esoteric_organisation"
version = "0.1.5"
description = "The core plugin of The Slimy Swamp Minecraft server."

val projectNameString = rootProject.name

val projectGroupString = group.toString()
val projectVersionString = version.toString()

val javaVersion = 21
val javaVersionEnumMember = JavaVersion.valueOf("VERSION_$javaVersion")

java {
    sourceCompatibility = javaVersionEnumMember
    targetCompatibility = javaVersionEnumMember

    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
}

dependencies {
    implementation("org.mongodb", "mongodb-driver-sync", "5.1.1")

    implementation("dev.jorel", "commandapi-bukkit-shade", "9.5.2")
    implementation("cloud.commandframework", "cloud-paper", "1.8.4")

    implementation("net.dv8tion", "JDA", "5.0.0")

    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveFileName.set("$projectNameString-$projectVersionString.jar")
    }

    compileJava {
        options.release.set(javaVersion)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
}

bukkitPluginYaml {
    main = "org.esoteric_organisation.tss_core_plugin.TSSCorePlugin"
    load = BukkitPluginYaml.PluginLoadOrder.STARTUP
    authors.addAll("Esoteric Organisation", "Esoteric Enderman")
    apiVersion = "1.21"
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = projectGroupString
            artifactId = projectNameString
            version = projectVersionString
        }
    }
}

tasks.named("publishMavenJavaPublicationToMavenLocal") {
    dependsOn(tasks.named("build"))
}
