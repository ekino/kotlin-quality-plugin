package com.ekino.oss.plugin

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin.BUILD_GROUP
import org.gradle.api.plugins.JavaPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoReportAggregationPlugin
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin
import org.sonarqube.gradle.SonarExtension
import org.sonarqube.gradle.SonarQubePlugin
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class KotlinQualityPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    with(project) {
      val extension = extensions.create<KotlinQualityPluginExtension>("kotlinQuality")

      apply<JavaPlugin>()
      apply<JacocoPlugin>()
      apply<SonarQubePlugin>()
      apply<KtlintPlugin>()
      apply<DetektPlugin>()
      apply<JacocoReportAggregationPlugin>()

      val jacocoTestReports = tasks.withType<JacocoReport>().also {
        it.configureEach {
          reports {
            xml.required.set(true)
            csv.required.set(false)
            html.required.set(true)
          }
        }
      }

      configure<JacocoPluginExtension> {
        toolVersion = "0.8.8"
      }

      configure<SonarExtension> {
        properties {
          property("sonar.projectName", project.name)
          property("sonar.sourceEncoding", "UTF-8")
          property("sonar.host.url", extension.sonarUrl)
          property("sonar.coverage.jacoco.xmlReportPaths", "${layout.buildDirectory}/reports/jacoco/test/jacocoTestReport.xml")
          property("sonar.gradle.skipCompile", extension.skipCompile)
        }
      }

      configure<KtlintExtension> {
        filter {
          include("src/**/*.kt")
        }
      }

      afterEvaluate {
        configureDetekt(project)
      }

      tasks.named(BUILD_GROUP) {
        dependsOn(jacocoTestReports)
      }
    }
  }

  private fun configureDetekt(project: Project) {
    with(project) {
      val configTempFile = Files.createTempFile("detekt-config-", ".yml")
      Files.copy(
        KotlinQualityPlugin::class.java.getResourceAsStream("/detekt-config.yml")!!,
        configTempFile,
        StandardCopyOption.REPLACE_EXISTING
      )

      val detektConfigFiles = mutableListOf(configTempFile)
      val customDetektConfig = rootDir.toPath().resolve(extensions.getByType<KotlinQualityPluginExtension>().customDetektConfig)
      if (Files.exists(customDetektConfig)) {
        logger.info("Applying custom detekt config")
        detektConfigFiles.add(customDetektConfig)
      } else {
        logger.info("No custom detekt config found")
      }

      configure<DetektExtension> {
        config.from(files(detektConfigFiles))
        buildUponDefaultConfig = true
      }

      tasks.withType<Detekt>().configureEach {
        doLast {
          Files.deleteIfExists(configTempFile)
        }
      }
    }
  }
}
