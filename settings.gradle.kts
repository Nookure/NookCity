rootProject.name = "NookCity"
include("NookCity-API")
include("NookCity-Velocity")

pluginManagement {
  repositories {
    maven {
      url = uri("https://repo.kyngs.xyz/gradle-plugins")
    }
    gradlePluginPortal()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
