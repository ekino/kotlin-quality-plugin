# kotlin quality plugin

Kotlin Quality plugin for Ekino projects

[![Build Status](https://travis-ci.org/ekino/kotlin-quality-plugin.svg?branch=master)](https://travis-ci.org/ekino/kotlin-quality-plugin)
[![GitHub (pre-)release](https://img.shields.io/github/release/ekino/kotlin-quality-plugin.svg)](https://github.com/ekino/kotlin-quality-plugin/releases)
[![GitHub license](https://img.shields.io/github/license/ekino/kotlin-quality-plugin.svg)](https://github.com/ekino/kotlin-quality-plugin/blob/master/LICENSE.md)

## Overview

This plugin configures the following tasks for any Ekino Java project :

* Apply the [Gradle Java plugin](https://docs.gradle.org/current/userguide/java_plugin.html)
* Apply the [Gradle Jacoco plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html)
* Apply the [Gradle SonarQube plugin](https://plugins.gradle.org/plugin/org.sonarqube)
* Apply the [Gradle Ktlint plugin](https://plugins.gradle.org/plugin/org.jlleitschuh.gradle.ktlint)
* Apply the [Gradle Detekt plugin](https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt)

## Requirement

You need to have a JDK 8 at least.

Gradle 5.0 or newer is required.
