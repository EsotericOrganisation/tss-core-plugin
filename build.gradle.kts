plugins {
    java
    application

    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("xyz.jpenilla.run-paper") version "2.1.0"
}

group = "net.slqmy"
version = "0.1"
description = "The core plugin of The Slimy Swamp Minecraft server. This plugin manages the connection to the database, player profiles and utility methods."

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()

    maven("https://repo.purpurmc.org/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    compileOnly("org.purpurmc.purpur", "purpur-api", "1.21-R0.1-SNAPSHOT")

    implementation("org.mongodb", "mongodb-driver-sync", "5.1.1")

    implementation("dev.jorel", "commandapi-bukkit-shade", "9.5.0")
    implementation("cloud.commandframework", "cloud-paper", "1.8.4")

    implementation("net.dv8tion", "JDA", "5.0.0")

    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()

        val props = mapOf(
                "name" to project.name,
                "version" to project.version,
                "description" to project.description,
                "apiVersion" to "1.21"
        )

        inputs.properties(props)

        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    runServer {
        minecraftVersion("1.21")
    }

    shadowJar {
        fun reloc(pkg: String) = relocate(pkg, "net.slqmy.tss_core.shaded.$pkg")

        reloc("cloud.commandframework")
        reloc("io.leangen.geantyref")
        reloc("net.dv8tion")
    }
}

application {
    mainClass.set("TSSCorePlugin")
}
