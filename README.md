# kotlin quality plugin

Kotlin Quality plugin for ekino projects

[![GitHub Workflow Status (master)](https://img.shields.io/github/workflow/status/ekino/kotlin-quality-plugin/Build%20branch/master.svg)](https://github.com/ekino/kotlin-quality-plugin/actions?query=workflow%3A%22Build+branch%22+branch%3A%22master%22+)
[![GitHub (pre-)release](https://img.shields.io/github/release/ekino/kotlin-quality-plugin.svg)](https://github.com/ekino/kotlin-quality-plugin/releases)
[![GitHub license](https://img.shields.io/github/license/ekino/kotlin-quality-plugin.svg)](https://github.com/ekino/kotlin-quality-plugin/blob/master/LICENSE.md)

## Overview

https://plugins.gradle.org/plugin/com.ekino.oss.plugin.kotlin-quality

This plugin configures the following tasks for any ekino Kotlin project :

* Apply the [Gradle Java plugin](https://docs.gradle.org/current/userguide/java_plugin.html)
* Apply the [Gradle Jacoco plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html)
* Apply the [Gradle SonarQube plugin](https://plugins.gradle.org/plugin/org.sonarqube)
* Apply the [Gradle Ktlint plugin](https://plugins.gradle.org/plugin/org.jlleitschuh.gradle.ktlint)
* Apply the [Gradle Detekt plugin](https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt)

## Requirements

You need to have a JDK 11 at least.

It requires Gradle 6.9.2 or later.

## Usage

The plugin provides a default configuration for detekt.
However, you can override it with your own in a `detekt-config-custom.yml` file at the root of your project

### Configuration

The plugin provides some settings :
```kotlin
configure<KotlinQualityPluginExtension> {
  customDetektConfig = "my-detekt.yml" // custom name for your detekt config, detekt-config-custom.yml by default
  sonarUrl = "https://my-sonar.com"
}
```
or
```kotlin
kotlinQuality {
  ...
}
```
