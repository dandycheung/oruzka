plugins { id("io.vacco.oss.gitflow") version "0.9.0" }

group = "io.vacco.oruzka"
version = "0.1.2"

configure<io.vacco.oss.gitflow.GsPluginProfileExtension> {
  addJ8Spec()
  addPmd()
  addClasspathHell()
  sharedLibrary(true, true)
}

configure<JavaPluginExtension> {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
  testImplementation("com.esotericsoftware.yamlbeans:yamlbeans:1.15")
}
