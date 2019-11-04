package com.ekino.oss.plugin

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin
import org.sonarqube.gradle.SonarQubeExtension
import org.sonarqube.gradle.SonarQubePlugin
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * Kotlin Quality plugin.
 */
class KotlinQualityPlugin : Plugin<Project> {
  override fun apply(project: Project) {

    with(project) {

      plugins.apply(JavaPlugin::class.java)
      plugins.apply(JacocoPlugin::class.java)
      plugins.apply(SonarQubePlugin::class.java)
      plugins.apply(KtlintPlugin::class.java)
      plugins.apply(DetektPlugin::class.java)

      val jacocoXmlReportPath = "$buildDir/reports/jacoco/test/jacoco.xml"

      tasks.named("jacocoTestReport", JacocoReport::class.java).configure {
        reports {
          xml.isEnabled = true
          xml.destination = file(jacocoXmlReportPath)
          csv.isEnabled = false
          html.isEnabled = false
        }
      }

      configure<JacocoPluginExtension> {
        toolVersion = "0.8.5"
      }

      configure<SonarQubeExtension> {
        properties {
          property("sonar.projectName", "oss-${project.name}")
          property("sonar.sourceEncoding", "UTF-8")
          property("sonar.host.url", "https://sonar.ekino.com")
          property("sonar.coverage.jacoco.xmlReportPaths", jacocoXmlReportPath)
        }
      }

      configure<KtlintExtension> {
        filter {
          include("src/**/*.kt")
        }
      }

      configureDetekt(project)

      tasks["build"].dependsOn("jacocoTestReport")
    }
  }

  private fun configureDetekt(project: Project) {
    with(project) {
      val configTempFile = Files.createTempFile("detekt-config-", ".yml")
      Files.copy(
        KotlinQualityPlugin::class.java.getResourceAsStream("/detekt-config.yml"),
        configTempFile,
        StandardCopyOption.REPLACE_EXISTING
      )

      configure<DetektExtension> {
        config = files(configTempFile)
        buildUponDefaultConfig = true
      }

      tasks.withType(Detekt::class) {
        doLast {
          Files.deleteIfExists(configTempFile)
        }
      }
    }
  }
}
