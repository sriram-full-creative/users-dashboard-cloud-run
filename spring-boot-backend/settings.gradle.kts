rootProject.name = "spring-boot-backend"

dependencyResolutionManagement {
  versionCatalogs {
    val springBootVersion = "3.4.4"
    // Main Libraries catalog
    create("libs") {
      version("springBoot", springBootVersion)
      version("mapstruct", "1.6.3")

      library(
              "springBootStarterWeb",
              "org.springframework.boot",
              "spring-boot-starter-web",
          )
          .versionRef("springBoot")
      library(
              "springBootStarterDataJpa",
              "org.springframework.boot",
              "spring-boot-starter-data-jpa",
          )
          .versionRef("springBoot")

      library("h2Database", "com.h2database", "h2").version("2.3.232")
      library("mapstruct", "org.mapstruct", "mapstruct").versionRef("mapstruct")
      library("mapstructProcessor", "org.mapstruct", "mapstruct-processor")
          .versionRef("mapstruct")
      library("lombok", "org.projectlombok", "lombok").version {
        strictly("1.18.36")
      }
    

      bundle(
          "springBootImplementation",
          listOf(
              "springBootStarterWeb",
              "springBootStarterDataJpa"
          ),
      )
      bundle("annotationProcessors", listOf("lombok", "mapstructProcessor"))
    }

      // Test libs
      create("testLibs") {
          version("springBootTest", springBootVersion)
          library(
              "springBootTestStarter",
              "org.springframework.boot",
              "spring-boot-starter-test",
          )
              .versionRef("springBootTest")
          library("junitJupiter", "org.testcontainers", "junit-jupiter")
              .version("1.20.4")
      }

    // Plugins catalog
    create("configLibs") {
      version("springBootPlugin", springBootVersion)
      version("springDependencyManagementPlugin", "1.1.6")
      version("spotlessPlugin", "7.0.0.BETA4")

      plugin("springBoot", "org.springframework.boot")
          .versionRef("springBootPlugin")
      plugin("springDependencyManagement", "io.spring.dependency-management")
          .versionRef("springDependencyManagementPlugin")
    }
  }
}
