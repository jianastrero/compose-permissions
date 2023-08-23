import java.io.FileInputStream
import java.net.URI
import java.util.*

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(FileInputStream(localPropertiesFile))
    }
}

val ossrhUsername: String? by localProperties
val ossrhPassword: String? by localProperties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("signing")
}

publishing {
    repositories {
        maven {
            val releaseRepo = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
//            val snapshotRepo = "https://s01.oss.sonatype.org/content/repositories/snapshots/"

            name = "OSSRH"
            url = URI.create(releaseRepo)

            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }
    publications {
        create<MavenPublication>("release") {

            groupId = "dev.jianastrero.compose-permissions"
            artifactId = "compose-permissions"
            version = "1.0.0"

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name.set("Compose Permissions")
                description.set("A simple library to minimize boilerplate for implementing requesting and checking if permissions are granted or not.")
                url.set("ttps://github.com/jianastrero/compose-permissions")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/jianastrero/compose-permissions/blob/main/LICENSE")
                        distribution.set("repo")
                    }
                }

                scm {
                    url.set("https://github.com/jianastrero/compose-permissions")
                    connection.set("scm:git@github.com:jianastrero/compose-permissions.git")
                    developerConnection.set("scm:git@github.com:jianastrero/compose-permissions.git")
                }

                developers {
                    developer {
                        id.set("jianastrero")
                        name.set("Jian James Astrero")
                        email.set("jianjamesastrero@gmail.com")
                        organizationUrl.set("https://jianastrero.dev/")
                    }
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["release"])
}

android {
    namespace = "dev.jianastrero.compose_permissions"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        aarMetadata {
            minCompileSdk = 24
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation("androidx.activity:activity-compose:1.7.2")

    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
}
