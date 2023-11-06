import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
  `kotlin-dsl`
  `java-gradle-plugin`

  jacoco

  id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
  id("io.gitlab.arturbosch.detekt") version "1.23.1"
  id("com.gradle.plugin-publish") version "1.2.1"
}

group = "com.ekino.oss.plugin"
version = "4.2.0"
findProperty("releaseVersion")?.let { version = it }

repositories {
  mavenCentral()
  maven { setUrl("https://plugins.gradle.org/m2/") }
}

dependencies {
  implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:4.0.0.2929")
  implementation("org.jlleitschuh.gradle.ktlint:org.jlleitschuh.gradle.ktlint.gradle.plugin:11.3.1")
  implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.3")

  testImplementation(gradleTestKit())
  testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.27.0")
  testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

tasks {
  test {
    useJUnitPlatform()
    jvmArgs = listOf("-Duser.language=en")
  }

  jacocoTestReport {
    reports {
      xml.required.set(true)
      html.required.set(false)
    }
  }
}

configure<DetektExtension> {
  buildUponDefaultConfig = true
  config = files("src/main/resources/detekt-config.yml")
}

gradlePlugin {
  website.set("https://github.com/ekino/kotlin-quality-plugin")
  vcsUrl.set("https://github.com/ekino/kotlin-quality-plugin")
  plugins {
    create("kotlin-quality") {
      id = "com.ekino.oss.plugin.kotlin-quality"
      implementationClass = "com.ekino.oss.plugin.KotlinQualityPlugin"
      displayName = "Kotlin Quality plugin"
      description = "Kotlin Quality plugin for Ekino projects"
      tags.set(listOf("ekino", "kotlin", "quality", "ktlint", "detekt", "sonarqube"))
    }
  }
}

ktlint {
  filter {
    include("src/**/*.kt")
  }
}
