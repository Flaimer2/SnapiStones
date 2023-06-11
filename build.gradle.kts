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
version = "2.0.0-alpha"

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
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    annotationProcessor("org.projectlombok:lombok:1.18.26")

    compileOnly("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.26")
    compileOnly("org.jetbrains:annotations:23.0.0")
    compileOnly("com.google.code.gson:gson:2.8.9")
    compileOnly("com.sk89q.worldguard:worldguard-legacy:6.2")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:6.1.3-SNAPSHOT")
    // Module Hologram
    compileOnly("com.github.decentsoftware-eu:decentholograms:2.8.1")
    compileOnly("me.clip:placeholderapi:2.11.3")

    implementation("space.arim.dazzleconf:dazzleconf-ext-snakeyaml:1.2.1")
    implementation("net.kyori:adventure-platform-bukkit:4.2.0")
    implementation("net.kyori:adventure-text-minimessage:4.12.0")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("co.aikar:idb-core:1.0.0-SNAPSHOT")
    implementation("de.tr7zw:item-nbt-api:2.11.2")
    implementation("commons-collections:commons-collections:3.2.2")
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
        relocateDependency("net.kyori")
        relocateDependency("org.yaml")
        relocateDependency("com.zaxxer")
        relocateDependency("de.tr7zw")
        relocate("co.aikar.commands", "$libraryPackage.acf")
        relocate("co.aikar.idb", "$libraryPackage.idb")
        relocate("co.aikar.locales", "$libraryPackage.locales")
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
    version = "2.0.0-alpha"
    main = "ru.mcsnapix.snapistones.plugin.SnapiStones"
    description = "Плагин на блоки привата"
    depend = listOf("WorldEdit", "WorldGuard")
    softDepend = listOf("PlaceholderAPI", "DecentHolograms")
    website = "https://mcsnapix.ru"
    authors = listOf("SnapiX", "Flaimer")
}