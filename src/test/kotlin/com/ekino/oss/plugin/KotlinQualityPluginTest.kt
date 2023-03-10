package com.ekino.oss.plugin

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

/**
 * Unit test class for [com.ekino.oss.plugin.KotlinQualityPlugin].
 */
class KotlinQualityPluginTest {

  @Test
  fun `Should contain configured tasks`() {
    val project: Project = ProjectBuilder.builder().build()

    project.plugins.apply("com.ekino.oss.plugin.kotlin-quality")

    assertThat(project.tasks.named("jacocoTestReport")).isNotNull()
    assertThat(project.tasks.named("sonarqube")).isNotNull()
    assertThat(project.tasks.named("build")).isNotNull()
  }

  @Test
  fun `Should contain applied plugins`() {
    val project = ProjectBuilder.builder().build()

    assertThat(project.plugins).isEmpty()

    project.plugins.apply("com.ekino.oss.plugin.kotlin-quality")

    assertThat(project.pluginManager.hasPlugin("java")).isTrue()
    assertThat(project.pluginManager.hasPlugin("jacoco")).isTrue()
    assertThat(project.pluginManager.hasPlugin("org.sonarqube")).isTrue()
    assertThat(project.pluginManager.hasPlugin("org.jlleitschuh.gradle.ktlint")).isTrue()
    assertThat(project.pluginManager.hasPlugin("io.gitlab.arturbosch.detekt")).isTrue()
  }
}
