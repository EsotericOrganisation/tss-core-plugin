plugins {
    java
    application

    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.papermc.paperweight.userdev") version "1.5.5"
    id("xyz.jpenilla.run-paper") version "2.1.0"
}

group = "net.slqmy"
version = "0.1"
description = "The core plugin of The Slimy Swamp Minecraft server. This plugin manages the connection to the database, player profiles and utility methods."

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()

    maven("https://repo.purpurmc.org/snapshots")
    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    compileOnly("org.purpurmc.purpur", "purpur-api", "1.20.1-R0.1-SNAPSHOT")

    implementation("org.mongodb", "mongodb-driver-sync", "4.10.2")
    compileOnly("net.luckperms", "api", "5.4")

    implementation("dev.jorel", "commandapi-bukkit-shade", "9.0.3")

    paperweight.paperDevBundle("1.20.1-R0.1-SNAPSHOT")
    implementation("cloud.commandframework", "cloud-paper", "1.8.3")
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
        options.release.set(17)
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
                "apiVersion" to "1.20"
        )

        inputs.properties(props)

        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    runServer {
        minecraftVersion("1.20.1")
    }

    shadowJar {
        fun reloc(pkg: String) = relocate(pkg, "net.slqmy.tss_core.shaded.$pkg")

        reloc("cloud.commandframework")
        reloc("io.leangen.geantyref")
    }
}

application {
    mainClass.set("TSSCorePlugin")
}
