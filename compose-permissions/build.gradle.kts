import java.io.FileInputStream
import java.util.*

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(FileInputStream(localPropertiesFile))
    }
}

val githubUsername: String? by localProperties
val githubPassword: String? by localProperties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

task("sourceJar", type = Jar::class) {
    from(android.sourceSets.maybeCreate("main").java.srcDirs)
    archiveClassifier.set("sources")
}

repositories {
    google()
    mavenCentral()

    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/jianastrero/compose-permissions")
        credentials {
            username = githubUsername
            password = githubPassword
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "dev.jianastrero"
            artifactId = "compose-permissions"
            version = "1.0.0"
            artifact("sourceJar")
            artifact("$buildDir/outputs/aar/compose-permissions-release.aar")

            pom {
                withXml {
                    val dependenciesNode = asNode().appendNode("dependencies")

                    project.configurations.implementation.get().allDependencies.forEach {
                        if (it.group != null || it.version != null || it.name == "unspecified") return@forEach

                        val dependencyNode = dependenciesNode.appendNode("dependency")
                        dependencyNode.appendNode("groupId", it.group)
                        dependencyNode.appendNode("artifactId", it.name)
                        dependencyNode.appendNode("version", it.version)
                    }
                }
            }
        }
    }
}

android {
    namespace = "dev.jianastrero.compose_permissions"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
}

dependencies {
    implementation("androidx.activity:activity-compose:1.7.2")

    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
}
