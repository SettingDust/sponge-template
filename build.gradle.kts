plugins {
    val kotlinVersion = "1.4.0"
    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion

    `maven-publish`

    id("com.github.johnrengelman.shadow") version "6.0.0"
    id("org.jmailen.kotlinter") version "3.0.2"
    id("net.kyori.blossom")
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
    val kotlin = kotlin("stdlib-jdk8", "1.4.0")
    implementation(kotlin)
    shadow(kotlin)

    val spongeApi = "org.spongepowered:spongeapi:7.3.0"
    implementation(spongeApi)
    kapt(spongeApi)

    val bstats = "org.bstats:bstats-sponge:1.7"
    implementation(bstats)
    kapt(bstats)

    val laven = "me.settingdust:laven-sponge"
    api(laven)
    shadow(laven) {
        exclude("org.spongepowered")
    }
}

tasks {
    compileKotlin { kotlinOptions.jvmTarget = "1.8" }
    compileTestKotlin { kotlinOptions.jvmTarget = "1.8" }
    jar { enabled = false }
    shadowJar {
        archiveClassifier.set("")
        configurations = listOf(project.configurations.shadow.get())

        relocate("kotlin", "${project.group}.runtime.kotlin")
        exclude("META-INF/**")

        artifacts.archives(archiveFile) { builtBy(shadowJar) }
    }
    build { dependsOn("formatKotlinMain") }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/SettingDust/more-placeholder")
            credentials {
                username = project.findProperty("gpr.user") as? String ?: System.getenv("GPR_USER")
                password = project.findProperty("gpr.key") as? String ?: System.getenv("GPR_API_KEY")
            }
        }
    }
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}

blossom {
    replaceToken("@version@", mainVersion)
}

kotlinter {
    ignoreFailures = true
}