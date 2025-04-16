plugins {
	java
  	alias(configLibs.plugins.springBoot)
  	alias(configLibs.plugins.springDependencyManagement)
}

group = "com.app"
version = "0.1.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(libs.bundles.springBootImplementation)
  	implementation(libs.mapstruct)
	runtimeOnly(libs.h2Database)
	compileOnly(libs.lombok)
	annotationProcessor(libs.bundles.annotationProcessors)
	testImplementation(testLibs.springBootTestStarter)
	testImplementation(testLibs.junitJupiter)
}

tasks.withType<Test> {
	useJUnitPlatform()
}
