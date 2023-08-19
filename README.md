# Compose Permissions

Streamline permission handling in Android Jetpack Compose applications with Compose Permissions.

A simple library to minimize boilerplate for implementing requesting and checking if permissions are granted or
not.

## Overview

**"Compose Permissions"** is an Android library tailored to simplify the process of managing permissions within Jetpack Compose projects. Say goodbye to the verbosity of permission requests and checks, and welcome a cleaner, more efficient way to handle user permissions.

## Demo

<img src="assets/demo.gif" width="300" alt="Compose Permissions Demo GIF">

## Installation

To integrate "Compose Permissions" into your project, follow these steps:

1. Add the Maven repository to your settings.gradle.kts or repositories block:

```kotlin
repositories {
   // ...
   maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/jianastrero/compose-permissions")
      credentials {
         username = YOUR_GITHUB_USERNAME
         password = YOUR_GITHUB_PERSONAL_TOKEN_CLASSIC
      }
   }
   // ...
}
```

2. Include the library in your app-level build.gradle.kts:

```kotlin
// Compose Permissions
implementation("dev.jianastrero:compose-permissions:1.0.1")
```

## Usage

1. Create a `ComposePermission` instance for the desired Android permission:

```kotlin
// Compose Permissions
val cameraPermission = composePermission(android.Manifest.permission.CAMERA)
```

2. Request the permission using a concise line of code:

```kotlin
// Compose Permissions
cameraPermission.request()
```

_Optionally, use the isGranted property to avoid unnecessary requests:_

```kotlin
if (!cameraPermission.isGranted) {
    cameraPermission.request()
}
```

## Example

```kotlin
val cameraPermission = composePermission(android.Manifest.permission.CAMERA)

if (!cameraPermission.isGranted) {
   cameraPermission.request()
}
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
