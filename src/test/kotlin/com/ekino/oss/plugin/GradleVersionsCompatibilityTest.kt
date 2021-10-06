package com.ekino.oss.plugin

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.File

/**
 * Check that plugin works across the range of supported Gradle version.
 */
class GradleVersionsCompatibilityTest {

  @TempDir
  lateinit var tempDir: File

  @ValueSource(strings = ["6.4.1", "6.5.1", "6.6.1", "6.8.3", "6.9", "7.0.2", "7.1.1", "7.2"])
  @ParameterizedTest(name = "Gradle {0}")
  @DisplayName("Should work in Gradle version")
  fun shouldWorkInGradleVersion(gradleVersion: String) {
    val buildScript =
      """
      plugins {
          id 'com.ekino.oss.plugin.kotlin-quality'
      }
      
      repositories {
          mavenCentral()
      }
      """

    File("$tempDir/build.gradle").writeText(buildScript)

    val result = GradleRunner.create()
      .withProjectDir(tempDir)
      .withGradleVersion(gradleVersion)
      .withPluginClasspath()
      .withArguments("build", "--stacktrace")
      .forwardOutput()
      .build()

    assertThat(result.task(":build")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
  }
}
