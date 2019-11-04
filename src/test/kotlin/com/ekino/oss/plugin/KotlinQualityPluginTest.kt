package com.ekino.oss.plugin

import org.assertj.core.api.Assertions.assertThat
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * Unit test class for [com.ekino.oss.plugin.KotlinQualityPlugin].
 */
class KotlinQualityPluginTest {

  @Test
  @DisplayName("should contain configured tasks")
  fun qualityPlugin() {

    val project: Project = ProjectBuilder.builder()
      .build()

    project.plugins.apply("com.ekino.oss.plugin.kotlin-quality")

    assertThat(project.tasks)
      .extracting("name")
      .contains("jacocoTestReport", "sonarqube", "build")
  }
}
