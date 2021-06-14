import kotlin.math.max

plugins {
    kotlin("jvm") version Versions.Kotlin apply false
    id("java")
}

allprojects {
    val project = this
    group = "org.snowshoe.community"
    version = snowshoeArtifactVersion()

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply {
        Plugins.Kotlin.addTo(this)
        Plugins.Idea.addTo(this)
        plugin("java")
    }

    repositories {
        mavenCentral()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"

        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xjsr305=strict",
                "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi"
            )
            jvmTarget = "11"
            allWarningsAsErrors = true
        }
    }

    tasks.withType<Test> {
        maxParallelForks = max(1, (Platform.availableProcessors / 2) - 1)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}