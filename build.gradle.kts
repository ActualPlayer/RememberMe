plugins {
    java
    id("net.kyori.blossom") version "1.1.0"
    id("com.github.johnrengelman.shadow") version "4.0.4"
}

group = "com.actualplayer"
version = "1.2.1"
description = "A way for the user to login to the server he last connected to."

repositories {
    mavenCentral()
    maven {
        name = "velocity"
        setUrl("https://repo.velocitypowered.com/snapshots/")
    }
    maven {
        setUrl("https://repo.spongepowered.org/maven")
    }
    maven {
        setUrl("https://repo.velocitypowered.com/snapshots/")
    }
    maven {
        setUrl("https://libraries.minecraft.net/")
    }
}

defaultTasks("clean", "build", "shadowJar")

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.8")
    annotationProcessor("org.projectlombok:lombok:1.18.8")

    implementation("net.luckperms:api:5.1")

    compileOnly("com.velocitypowered:velocity-api:1.1.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:1.1.0-SNAPSHOT")

    testImplementation("junit", "junit", "4.12")
}

blossom {
    replaceToken("@ID@", name.replace(" ", "").toLowerCase())
    replaceToken("@NAME@", name)
    replaceToken("@VERSION@", version)
    replaceToken("@DESCRIPTION@", description)
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

configurations {
    runtime {
        exclude(group = "me.lucko.luckperms")
    }
}