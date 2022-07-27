import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
  `kotlin-dsl`
  `java-gradle-plugin`

  id("org.sonarqube") version "3.4.0.2513"
  jacoco

  id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
  id("io.gitlab.arturbosch.detekt") version "1.21.0"
  id("com.gradle.plugin-publish") version "1.0.0"
}

group = "com.ekino.oss.plugin"
version = "3.3.1"
findProperty("releaseVersion")?.let { version = it }

repositories {
  mavenCentral()
  maven { setUrl("https://plugins.gradle.org/m2/") }
}

dependencies {
  implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:3.3")
  implementation("org.jlleitschuh.gradle:ktlint-gradle:10.1.0")
  implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.21.0")

  testImplementation(gradleTestKit())
  testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
  testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
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
  plugins {
    create("kotlin-quality") {
      id = "com.ekino.oss.plugin.kotlin-quality"
      implementationClass = "com.ekino.oss.plugin.KotlinQualityPlugin"
      displayName = "Kotlin Quality plugin"
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
  tags = listOf("ekino", "kotlin", "quality", "ktlint", "detekt", "sonarqube")
}
