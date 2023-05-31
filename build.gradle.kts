plugins {
    id("java")
    id("org.sonarqube") version "3.5.0.2730"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val shadowJar: com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar by tasks
shadowJar.apply {
    mergeServiceFiles()
    archiveFileName.set("${project.name}.jar")
}

group = "ru.mcsnapix"
version = "1.0.0-alpha"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    annotationProcessor("org.projectlombok:lombok:1.18.26")

    compileOnly("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.26")
    compileOnly("org.jetbrains:annotations:23.0.0")
    compileOnly("com.google.code.gson:gson:2.8.7")
    compileOnly("com.sk89q.worldguard:worldguard-legacy:6.2")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:6.1.3-SNAPSHOT")
    // Module Hologram
    compileOnly("com.github.decentsoftware-eu:decentholograms:2.8.1")

    implementation("space.arim.dazzleconf:dazzleconf-ext-snakeyaml:1.2.1")
    implementation("net.kyori:adventure-platform-bukkit:4.2.0")
    implementation("net.kyori:adventure-text-minimessage:4.12.0")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("co.aikar:idb-core:1.0.0-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:2.4.1")
}

sonarqube {
  properties {
    property("sonar.projectKey", "Flaimer2_SnapiStones")
    property("sonar.organization", "flaimer2-1")
    property("sonar.host.url", "https://sonarcloud.io")
  }
}

val libraryPackage = "ru.mcsnapix.snapistones.libraries"

fun com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar.relocateDependency(pkg: String) {
    relocate(pkg, "$libraryPackage.$pkg")
}

tasks {
    shadowJar {
        relocateDependency("space.arim.dazzleconf")
        relocateDependency("org.yaml")
        relocateDependency("com.zaxxer")
        relocate("co.aikar.commands", "ru.mcsnapix.library.acf")
        relocate("co.aikar.idb", "ru.mcsnapix.library.idb")
        relocate("co.aikar.locales", "ru.mcsnapix.library.locales")
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(11)
        options.compilerArgs.addAll(listOf("-nowarn", "-Xlint:-unchecked", "-Xlint:-deprecation"))
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}

bukkit {
    name = "SnapiStones"
    version = "1.0.0-alpha"
    main = "ru.mcsnapix.snapistones.plugin.SnapiStones"
    description = "Плагин на блоки привата"
    depend = listOf("WorldEdit", "WorldGuard")
    website = "https://mcsnapix.ru"
    authors = listOf("SnapiX", "Flaimer")
}