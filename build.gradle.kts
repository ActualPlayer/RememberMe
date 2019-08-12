plugins {
    java
    id("net.kyori.blossom") version "1.1.0"
    id("com.github.johnrengelman.shadow") version "4.0.4"
}

group = "com.actualplayer"
version = "1.0"
description = "A way for the user to login to the server he last connected to."

repositories {
    mavenCentral()
    maven {
        name = "velocity"
        setUrl("https://repo.velocitypowered.com/snapshots/")
    }
    maven {
        name = "sponge"
        setUrl("https://repo.spongepowered.org/maven")
    }
}

defaultTasks("clean", "build", "shadowJar")

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.8")
    annotationProcessor("org.projectlombok:lombok:1.18.8")

    compileOnly("com.velocitypowered:velocity-api:1.0.3-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:1.0.3-SNAPSHOT")

    implementation("org.spongepowered:configurate-yaml:3.6") {
        exclude(group = "org.checkerframework", module = "checker-qual")
        exclude(group = "com.google.guava", module = "guava")
    }

    testImplementation("junit", "junit", "4.12")
}

blossom {
    replaceToken("@ID@", name.toLowerCase())
    replaceToken("@NAME@", name)
    replaceToken("@VERSION@", version)
    replaceToken("@DESCRIPTION@", description)
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}