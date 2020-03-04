package com.ekino.oss.plugin

import org.assertj.core.api.Assertions.assertThat
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

  @ValueSource(strings = ["5.4.1", "5.6.4", "6.1.1", "6.2.2"])
  @ParameterizedTest(name = "Gradle {0}")
  @DisplayName("Should work in Gradle version")
  fun shouldWorkInGradleVersion(gradleVersion: String) {
    val buildScript =
      """
      plugins {
          id 'com.ekino.oss.plugin.kotlin-quality'
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
