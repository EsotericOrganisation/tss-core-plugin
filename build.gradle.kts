import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
    `java-library`

    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.1.1"
    id("xyz.jpenilla.run-paper") version "2.3.0"
}

group = "net.slqmy"
version = "0.1"
description = "The core plugin of The Slimy Swamp Minecraft server. This plugin manages the connection to the database, player profiles and utility methods."

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

dependencies {
    implementation("org.mongodb", "mongodb-driver-sync", "5.1.1")

    implementation("dev.jorel", "commandapi-bukkit-shade", "9.5.0")
    implementation("cloud.commandframework", "cloud-paper", "1.8.4")

    implementation("net.dv8tion", "JDA", "5.0.0")

    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    compileJava {
        options.release.set(21)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
}

bukkitPluginYaml {
  main = "net.slqmy.tss_core.TSSCorePlugin"
  load = BukkitPluginYaml.PluginLoadOrder.STARTUP
  authors.add("Slqmy")
  apiVersion = "1.21"
}