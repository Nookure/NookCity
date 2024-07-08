plugins {
  id("java")
  alias(libs.plugins.shadowJar)
  alias(libs.plugins.grgit)
  id("xyz.jpenilla.run-velocity") version "2.3.0"
}

val versionCode = "1.0.0"
version = versionCode

group = "com.nookure.nookcity"

dependencies {
  implementation(project(":NookCity-API"))
  implementation(project(":NookCity-Velocity"))
  implementation(libs.bundles.nookcore)
}

subprojects {
  apply(plugin = "java")
  apply(plugin = "com.github.johnrengelman.shadow")

  dependencies {
    compileOnly(rootProject.libs.bundles.nookcore)
    compileOnly(rootProject.libs.guice)
  }
}

allprojects {
  repositories {
    mavenCentral()
    maven("https://maven.nookure.com")
    maven("https://papermc.io/repo/repository/maven-public/")
  }

  tasks {
    withType<JavaCompile> {
      options.encoding = "UTF-8"
    }

    withType<Javadoc> {
      options.encoding = "UTF-8"
    }
  }
}

tasks.shadowJar {
  relocate("org.spongepowered.configurate", "com.nookure.configurate")
}

tasks.test {
  useJUnitPlatform()
}

tasks {
  runVelocity {
    velocityVersion("3.3.0-SNAPSHOT")

    downloadPlugins {
      url("https://download.luckperms.net/1549/velocity/LuckPerms-Velocity-5.4.134.jar")
    }
  }
}

tasks.withType(xyz.jpenilla.runtask.task.AbstractRun::class) {
  javaLauncher = javaToolchains.launcherFor {
    vendor = JvmVendorSpec.JETBRAINS
    languageVersion = JavaLanguageVersion.of(17)
  }
  jvmArgs("-XX:+AllowEnhancedClassRedefinition", "-XX:+AllowRedefinitionToAddDeleteMethods")
  systemProperties["nookcity.debug"] = "true"
}