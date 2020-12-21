import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
  `kotlin-dsl`
  `java-gradle-plugin`

  id("org.sonarqube") version "3.0"
  jacoco

  id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
  id("io.gitlab.arturbosch.detekt") version "1.15.0"
  id("com.gradle.plugin-publish") version "0.12.0"
}

group = "com.ekino.oss.plugin"
version = "2.0.0"
findProperty("releaseVersion")?.let { version = it }

repositories {
  mavenCentral()
  maven { setUrl("https://plugins.gradle.org/m2/") }
}

dependencies {
  implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:3.0")
  implementation("org.jlleitschuh.gradle:ktlint-gradle:9.3.0")
  implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.14.2")

  testImplementation(gradleTestKit())
  testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
  testImplementation("org.assertj:assertj-core:3.18.1")
}

tasks {
  test {
    useJUnitPlatform()
    jvmArgs = listOf("-Duser.language=en")
  }
}

configure<DetektExtension> {
  buildUponDefaultConfig = true
  config = files("src/main/resources/detekt-config.yml")
}

gradlePlugin {
  (plugins) {
    create("kotlin-quality") {
      id = "com.ekino.oss.plugin.kotlin-quality"
      implementationClass = "com.ekino.oss.plugin.KotlinQualityPlugin"
    }
  }
}

ktlint {
  filter {
    include("src/**/*.kt")
  }
}

pluginBundle {
  website = "https://github.com/ekino/kotlin-quality-plugin"
  vcsUrl = "https://github.com/ekino/kotlin-quality-plugin"
  description = "Kotlin Quality plugin for Ekino projects"

  (plugins) {
    named("kotlin-quality") {
      displayName = "Kotlin Quality plugin"
      tags = listOf("ekino", "kotlin", "quality", "ktlint", "detekt", "sonarqube")
      version = version
    }
  }
}
