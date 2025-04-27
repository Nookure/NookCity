dependencies {
  compileOnly(libs.velocity)
  compileOnly(libs.miniMessage)
  annotationProcessor(libs.velocity)
  compileOnly(project(":api"))
}
