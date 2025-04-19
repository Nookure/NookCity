plugins {
  id("net.kyori.blossom") version "2.1.0"
}

dependencies {
  compileOnly(libs.velocity)
}

sourceSets {
  main {
    blossom {
      javaSources {
        property("version", rootProject.version.toString())
      }
    }
  }
}
