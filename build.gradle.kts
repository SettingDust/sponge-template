plugins {
    java
    `maven-publish`

    id("com.github.johnrengelman.shadow") version "6.0.0"
    id("net.kyori.blossom") version "1.1.0"
}

val major = 0
val minor = 0
val patch = 1

val mainVersion = arrayOf(major, minor, patch).joinToString(".")

group = "org.example"
version =
    "$mainVersion${
        @OptIn(ExperimentalStdlibApi::class)
        buildList {
            add("")
            if (System.getenv("BUILD_NUMBER") != null) {
                add(System.getenv("BUILD_NUMBER").toString())
            }
            if (System.getenv("GITHUB_REF") == null || System.getenv("GITHUB_REF").endsWith("-dev")) {
                add("unstable")
            }
        }.joinToString("-")
    }"

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/maven/")
    maven("https://repo.codemc.org/repository/maven-public")
}

dependencies {
    val spongeApi = "org.spongepowered:spongeapi:7.3.0"
    implementation(spongeApi)
    annotationProcessor(spongeApi)

    val bstats = "org.bstats:bstats-sponge:1.7"
    shadow(bstats)
    implementation(bstats)
    annotationProcessor(bstats)
}

tasks {
    jar { enabled = false }
    shadowJar {
        archiveClassifier.set("")
        configurations = listOf(project.configurations.shadow.get())

        exclude("META-INF/**")

        artifacts.archives(archiveFile) { builtBy(shadowJar) }
    }
}

blossom {
    replaceToken("@version@", mainVersion)
}