import org.gradle.api.plugins.ObjectConfigurationAction
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.ScriptHandlerScope
import org.gradle.kotlin.dsl.exclude
import org.gradle.plugin.use.PluginDependenciesSpec

object Versions {
    const val Detekt = "1.17.0"
    const val Kotlin = "1.5.10"
    const val KotlinCoroutines = "1.5.0"
    const val Protobuf = "3.17.1"
    const val Kafka = "2.7.0"
    const val SpringBoot = "2.4.5"
    const val KotlinLogging = "2.0.6"
    const val Retrofit = "2.9.0"
    const val OkHttp = "4.2.1"
    const val Reactor = "3.4.6"
    const val Exposed = "0.17.13"
}

object Plugins { // please keep this sorted in sections
    // Kotlin
    val Kotlin = PluginSpec("kotlin", Versions.Kotlin)

    // 3rd Party
    val Detekt = PluginSpec("io.gitlab.arturbosch.detekt", Versions.Detekt)
    val Flyway = PluginSpec("org.flywaydb.flyway", "7.7.0")
    val Idea = PluginSpec("idea")
    val Protobuf = PluginSpec("com.google.protobuf", "0.8.16")
    val SpringBoot = PluginSpec("org.springframework.boot", Versions.SpringBoot)
    val SpringDependencyManagement = PluginSpec("io.spring.dependency-management", "1.0.11.RELEASE")
}

object Dependencies {
    // Kotlin
    object Kotlin {
        val AllOpen = DependencySpec("org.jetbrains.kotlin:kotlin-allopen", Versions.Kotlin)
        val Reflect = DependencySpec("org.jetbrains.kotlin:kotlin-reflect", Versions.Kotlin)
        val StdlbJdk8 = DependencySpec("org.jetbrains.kotlin:kotlin-stdlib-jdk8", Versions.Kotlin)
        val CoroutinesCoreJvm = DependencySpec(
            "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm",
            Versions.KotlinCoroutines
        )
        val CoroutinesReactor = DependencySpec(
            "org.jetbrains.kotlinx:kotlinx-coroutines-reactor",
            Versions.KotlinCoroutines
        )
        val CoroutinesJdk8 = DependencySpec(
            "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8",
            Versions.KotlinCoroutines
        )
        val CoroutinesTest = DependencySpec(
            "org.jetbrains.kotlinx:kotlinx-coroutines-test",
            Versions.KotlinCoroutines
        )
    }

    // Spring Boot
    object SpringBoot {
        val Starter = DependencySpec("org.springframework.boot:spring-boot-starter")
        val StarterWebFlux = DependencySpec(
            name = "org.springframework.boot:spring-boot-starter-webflux",
            exclude = listOf("org.springframework.boot:spring-boot-starter-tomcat")
        )
        val StarterJetty = DependencySpec("org.springframework.boot:spring-boot-starter-jetty")
        val StarterActuator = DependencySpec("org.springframework.boot:spring-boot-starter-actuator")
        val StarterDevTools = DependencySpec("org.springframework.boot:spring-boot-devtools")
        val StarterSecurity = DependencySpec("org.springframework.boot:spring-boot-starter-security")
        val StarterValidation = DependencySpec("org.springframework.boot:spring-boot-starter-validation")

        val StarterTest =
            DependencySpec(
                name = "org.springframework.boot:spring-boot-starter-test",
                exclude = listOf(
                    "org.junit.vintage:junit-vintage-engine",
                    "org.mockito:mockito-core"
                )
            )
    }

    // Project Reactor
    object Reactor {
        // https://github.com/reactor/reactor-core
        val Core = DependencySpec("io.projectreactor:reactor-core", Versions.Reactor)
    }

    // Protobuf
    object Protobuf {
        val Java = DependencySpec("com.google.protobuf:protobuf-java", Versions.Protobuf)
        val JavaUtil = DependencySpec("com.google.protobuf:protobuf-java-util", Versions.Protobuf)
    }

    // Square's Retrofit API client
    object Retrofit {
        val Core = DependencySpec("com.squareup.retrofit2:retrofit", Versions.Retrofit)
        val JacksonConverter = DependencySpec("com.squareup.retrofit2:converter-jackson", Versions.Retrofit)
        val ScalarsConverter = DependencySpec("com.squareup.retrofit2:converter-scalars", Versions.Retrofit)
    }

    object OkHttp {
        val Core = DependencySpec("com.squareup.okhttp3:okhttp", Versions.OkHttp)
        val LoggingInterceptor = DependencySpec("com.squareup.okhttp3:logging-interceptor", Versions.OkHttp)
    }

    object Jackson {
        val KotlinModule = DependencySpec(
            "com.fasterxml.jackson.module:jackson-module-kotlin",
            "2.12.+"
        )
    }

    // Database
    val Postgres = DependencySpec("org.postgresql:postgresql", "42.2.19")

    // ORM
    object Exposed {
        val Core = DependencySpec("org.jetbrains.exposed:exposed", Versions.Exposed)
    }

    val KotlinLogging = DependencySpec("io.github.microutils:kotlin-logging-jvm", Versions.KotlinLogging)

    val Ktlint = DependencySpec("com.pinterest:ktlint", "0.41.0")
    val Mockk = DependencySpec("io.mockk:mockk", "1.11.0")
    val Hamkrest = DependencySpec("com.natpryce:hamkrest", "1.8.0.1")
    val Redisson = DependencySpec("org.redisson:redisson", "3.14.0")
    val SpringMockk = DependencySpec("com.ninja-squad:springmockk", "3.0.1")
    val KotlinFaker = DependencySpec("io.github.serpro69:kotlin-faker:1.8.0-rc.0")
}

data class PluginSpec(
    val id: String,
    val version: String = ""
) {
    fun addTo(scope: PluginDependenciesSpec) {
        scope.also {
            it.id(id).version(version.takeIf { v -> v.isNotEmpty() })
        }
    }

    fun addTo(action: ObjectConfigurationAction) {
        action.plugin(this.id)
    }
}

data class DependencySpec(
    val name: String,
    val version: String = "",
    val isChanging: Boolean = false,
    val exclude: List<String> = emptyList()
) {
    fun plugin(scope: PluginDependenciesSpec) {
        scope.apply {
            id(name).version(version.takeIf { it.isNotEmpty() })
        }
    }

    fun classpath(scope: ScriptHandlerScope) {
        val spec = this
        with(scope) {
            dependencies {
                classpath(spec.toDependencyNotation())
            }
        }
    }

    fun implementation(handler: DependencyHandlerScope) {
        val spec = this
        with(handler) {
            "implementation".invoke(spec.toDependencyNotation()) {
                isChanging = spec.isChanging
                spec.exclude.forEach { excludeDependencyNotation ->
                    val (group, module) = excludeDependencyNotation.split(":", limit = 2)
                    this.exclude(group = group, module = module)
                }
            }
        }
    }

    fun testImplementation(handler: DependencyHandlerScope) {
        val spec = this
        with(handler) {
            "testImplementation".invoke(spec.toDependencyNotation()) {
                isChanging = spec.isChanging
                spec.exclude.forEach { excludeDependencyNotation ->
                    val (group, module) = excludeDependencyNotation.split(":", limit = 2)
                    this.exclude(group = group, module = module)
                }
            }
        }
    }

    fun toDependencyNotation(): String =
        listOfNotNull(
            name,
            version.takeIf { it.isNotEmpty() }
        ).joinToString(":")
}