plugins { id("io.vacco.common-build") version "0.5.3" }

group = "io.vacco.oruzka"
version = "0.1.2"

configure<io.vacco.common.CbPluginProfileExtension> {
  addJ8Spec()
  addPmd()
  addSpotBugs()
  addClasspathHell()
  setPublishingUrlTransform { repo -> "${repo.url}/${project.name}" }
  sharedLibrary()
}

configure<JavaPluginExtension> {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
  testImplementation("com.esotericsoftware.yamlbeans:yamlbeans:1.15")
}