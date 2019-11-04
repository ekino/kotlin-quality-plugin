package com.ekino.oss.plugin

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
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
}
