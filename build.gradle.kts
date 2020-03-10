import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
  `kotlin-dsl`
  `java-gradle-plugin`

  id("org.sonarqube") version "2.8"
  jacoco

  id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
  id("io.gitlab.arturbosch.detekt") version "1.5.1"
  id("com.gradle.plugin-publish") version "0.10.1"
}

group = "com.ekino.oss.plugin"
version = "1.1.0"
findProperty("releaseVersion")?.let { version = it }

repositories {
  mavenCentral()
  maven { setUrl("https://plugins.gradle.org/m2/") }
}

dependencies {
  implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:2.8")
  implementation("org.jlleitschuh.gradle:ktlint-gradle:9.1.0")
  implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.1.1")

  testImplementation(gradleTestKit())
  testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
  testImplementation("org.assertj:assertj-core:3.15.0")
}

tasks {
  test {
    useJUnitPlatform()
    jvmArgs = listOf("-Duser.language=en")
  }
}

configure<DetektExtension> {
  buildUponDefaultConfig = true
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
