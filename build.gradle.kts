import org.gradle.kotlin.dsl.support.listFilesOrdered
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Duration
import java.util.*
import kotlin.io.path.writeText

plugins {
    java
    jacoco
    alias(libs.plugins.jmh)
}

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor(libs.lombok)
    implementation(libs.bundles.utils)

    testImplementation(libs.bundles.testing)
    testRuntimeOnly(libs.junit.platform.launcher)

    jmhAnnotationProcessor(libs.jmh.annprocess)
    jmhImplementation(libs.bundles.jmh)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}

val enablePreview = "--enable-preview"

tasks.compileJava {
    options.compilerArgs.add(enablePreview)
}

tasks.compileTestJava {
    options.compilerArgs.add(enablePreview)
}

tasks.jmhRunBytecodeGenerator {
    jvmArgs.add(enablePreview)
}

tasks.compileJmhJava {
    options.compilerArgs.add(enablePreview)
}

tasks.jmh {
    jvmArgsAppend.add(enablePreview)
}

tasks.test {
    jvmArgs(enablePreview)
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
    maxParallelForks = Runtime.getRuntime().availableProcessors()
}

tasks.jacocoTestReport {
    reports {
        csv.required = false
        xml.required = true
        html.required = true
    }
}

val year = 2024
val repository = "https://github.com/tinohertlein/advent-of-code-$year"

val resourcesPath: Path = Paths.get("src", "test", "resources")
val sourcesPath: Path = Paths.get("src", "main", "java", "dev", "hertlein", "aoc2024")
val testsPath: Path = Paths.get("src", "test", "java", "dev", "hertlein", "aoc2024")
val jmhSourcesPath: Path = Paths.get("src", "jmh", "java", "dev", "hertlein", "aoc2024")

private fun dayFromString(input: String): Int {
    return "\\d\\d"
        .toRegex()
        .find(input)
        ?.groups
        ?.get(0)
        ?.value
        ?.toInt()
        ?: error("Could not extract day from '$input'.")
}


tasks.register("setupNextDay") {
    group = "aoc"
    description = "Create the files for the next puzzle day."

    doLast {
        val templatePrefix = "Day00"

        val previousDay =
            dayFromString(file(sourcesPath).listFilesOrdered { it.name.startsWith("Day") }.last().name)
        check(previousDay <= 25) { "No more puzzles available this year." }

        val nextDay = String.format("%02d", previousDay + 1)

        fun copyTemplatesFrom(folder: File): Unit? {
            return folder
                .listFiles { file -> file.name.contains(templatePrefix) }
                ?.forEach { file ->
                    val nextDayFileContent = file.readText().replace(templatePrefix, "Day$nextDay")
                    val nextDayFileName = file.absolutePath.replace(templatePrefix, "Day$nextDay")
                    file(nextDayFileName).writeText(nextDayFileContent)
                }
        }

        listOf(resourcesPath, sourcesPath, testsPath, jmhSourcesPath)
            .map { file(it) }
            .forEach { copyTemplatesFrom(it) }
    }
}

tasks.register("downloadChallengeInput") {
    group = "aoc"
    description = "Download the challenge input for the next puzzle day from the advent-of-code website."

    doLast {
        val configFilename = ".aocconfig"
        val configFile = project.rootProject.file(configFilename)

        val currentDayFile = file(resourcesPath).listFilesOrdered { it.name.endsWith("-challenge.txt") }.last()
        check(currentDayFile.readText().isEmpty()) { "File '${currentDayFile.name}' is not empty." }

        val currentDay = dayFromString(currentDayFile.name)

        fun loadCookieValue(): String {
            check(configFile.exists()) { "Could not find config file '$configFilename'." }
            val properties = Properties()
            properties.load(configFile.inputStream())
            return properties.getProperty("COOKIE")
        }

        fun downloadChallengeInput(day: Int, file: Path) {
            HttpClient.newHttpClient().use { client ->
                val request = HttpRequest.newBuilder()
                    .uri(URI.create("https://adventofcode.com/$year/day/$day/input"))
                    .setHeader("Content-Type", "text/plain")
                    .setHeader("Cookie", "session=${loadCookieValue()}}")
                    .setHeader("User-Agent", repository)
                    .timeout(Duration.ofSeconds(5))
                    .build()

                val response = client.send(request, HttpResponse.BodyHandlers.ofString())
                check(response.statusCode() == 200) { "Download failed. ${response.statusCode()}: ${response.body()}" }

                file.writeText(response.body())
            }
        }
        downloadChallengeInput(currentDay, currentDayFile.toPath())
    }
}