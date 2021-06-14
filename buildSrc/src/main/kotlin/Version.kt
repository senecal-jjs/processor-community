import org.gradle.api.Project

fun Project.snowshoeArtifactVersion(): String = this.findProperty("artifactVersion")?.toString()
    ?: "1.0-snapshot"