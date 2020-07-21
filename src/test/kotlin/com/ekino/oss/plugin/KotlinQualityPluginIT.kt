package com.ekino.oss.plugin

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

/**
 * Integration test class for [com.ekino.oss.plugin.KotlinQualityPlugin].
 */
class KotlinQualityPluginIT {

  @Test
  fun `should publish plugin to maven local`(@TempDir tempDir: Path) {
    val buildFileContent =
      """
          plugins {
              id("com.ekino.oss.plugin.kotlin-quality")
          }
          group = "com.ekino.oss.test-quality"
          version = "1.0.0"
          repositories {
              mavenCentral()
          }
          
      """.trimIndent()

    val file = tempDir.resolve("build.gradle.kts")
    Files.write(file, buildFileContent.toByteArray())

    val result: BuildResult = GradleRunner.create()
      .withProjectDir(tempDir.toFile())
      .withArguments("build")
      .withPluginClasspath()
      .build()

    assertThat(result.output)
      .containsSubsequence("jacocoTestReport", "build")
  }

  @Test
  fun `must end with a newline`(@TempDir tempDir: Path) {
    val buildFileContent =
      """
          plugins {
              id("com.ekino.oss.plugin.kotlin-quality")
          }
          group = "com.ekino.oss.test-quality"
          version = "1.0.0"
          repositories {
              mavenCentral()
          } // no newline 
      """.trimIndent()

    val file = tempDir.resolve("build.gradle.kts")
    Files.write(file, buildFileContent.toByteArray())

    val result: BuildResult = GradleRunner.create()
      .withProjectDir(tempDir.toFile())
      .withArguments("build")
      .withPluginClasspath()
      .buildAndFail()

    assertThat(result.output)
      .containsSubsequence("must end with a newline")
  }

  @Test
  fun `unnecessary space(s)`(@TempDir tempDir: Path) {
    val buildFileContent =
      """
          plugins {
              id("com.ekino.oss.plugin.kotlin-quality")     // Unnecessary space(s)
          }
          group = "com.ekino.oss.test-quality"
          version = "1.0.0"
          repositories {
              mavenCentral()
          }
          
      """.trimIndent()

    val file = tempDir.resolve("build.gradle.kts")
    Files.write(file, buildFileContent.toByteArray())

    val result: BuildResult = GradleRunner.create()
      .withProjectDir(tempDir.toFile())
      .withArguments("build")
      .withPluginClasspath()
      .buildAndFail()

    assertThat(result.output)
      .containsSubsequence("Unnecessary space(s)")
  }

  @Test
  fun `custom detekt config with default name`(@TempDir tempDir: Path) {
    val result = runTask("project_with_custom_detekt", tempDir)

    assertThat(result.output)
      .containsSubsequence("EmptyClassBlock", "UnnecessaryAbstractClass")
  }

  @Test
  fun `custom detekt config with custom name`(@TempDir tempDir: Path) {
    val result = runTask("project_with_custom_detekt_extension", tempDir)

    assertThat(result.output)
      .containsSubsequence("EmptyClassBlock", "UnnecessaryAbstractClass")
  }

  @Test
  fun `without custom detekt config `(@TempDir tempDir: Path) {
    val result = runTask("project_without_custom_detekt", tempDir)

    assertThat(result.output)
      .containsSubsequence("SUCCESSFUL")
  }

  private fun runTask(project: String, tempDir: Path, task: String = "build"): BuildResult {
    File("src/test/resources/$project").copyRecursively(tempDir.toFile())

    return GradleRunner.create()
      .withArguments(task)
      .withProjectDir(tempDir.toFile())
      .withTestKitDir(tempDir.toFile())
      .withPluginClasspath()
      .forwardOutput()
      .build()
  }
}
