plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "me.crylonz"
version = "2.4.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.1-R0.1-SNAPSHOT")
    testImplementation("org.xerial:sqlite-jdbc:3.49.1.0")
    testImplementation("org.spigotmc:spigot-api:1.21.1-R0.1-SNAPSHOT")
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.0")
    testImplementation("org.mockito:mockito-core:5.16.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.12.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(17)
}

tasks.processResources {
    filteringCharset = "UTF-8"
    inputs.property("version", project.version)

    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}


tasks.shadowJar {
    archiveClassifier.set("")
}

tasks.test {
    useJUnitPlatform()
}
