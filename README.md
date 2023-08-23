# 😌 Compose Permissions

Streamline permission handling in Android Jetpack Compose applications with Compose Permissions.

A simple library to minimize boilerplate for implementing requesting and checking if permissions are granted or
not.

_Read more on [Medium](https://medium.com/@jianastrero/streamline-permission-handling-in-android-jetpack-compose-applications-with-compose-permissions-560b3b58fb70)._

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Static Badge](https://img.shields.io/badge/Jetpack_Compose-37bf6e?style=for-the-badge&logo=data%3Aimage%2Fpng%3Bbase64%2CiVBORw0KGgoAAAANSUhEUgAAAA0AAAAOCAYAAAD0f5bSAAAABHNCSVQICAgIfAhkiAAAAAFzUkdCAK7OHOkAAAAEZ0FNQQAAsY8L%2FGEFAAAACXBIWXMAAA7DAAAOwwHHb6hkAAAAX3pUWHRSYXcgcHJvZmlsZSB0eXBlIEFQUDEAAAiZ40pPzUstykxWKCjKT8vMSeVSAANjEy4TSxNLo0QDAwMLAwgwNDAwNgSSRkC2OVQo0QAFmJibpQGhuVmymSmIzwUAT7oVaBst2IwAAAFlSURBVChTY6AbYITScGD%2FtEfk37fvrQyMjBH%2FGRhmMTP%2Fbj2o2PgBKg0GcE32%2B%2BtZ%2FsmxZjD8%2B98E1CAIFWYAanzDyMjYwKT0a%2BZBxsY%2FIDEmsAwQfPjweS9QxWQgFvz74QvD76dvGP5%2B%2FMLA%2BO%2B%2FCFBsyv8dJvuhShmYoTTDx5ucM78%2F%2BMjMzfufIVDGgyFbMZGB7TcTw53bTxmYzwUyMN03kHiwr70FpBZuE8gh7448ZeBf%2B53BV8KNgZuZi8FPwp1B%2B4kHA8MrIagaCEDSBAEYIYMFoGpiZmE4fvwow%2BzpExk%2Bf%2FnKsOngLYar9z4yMDKzQxVAANxgDn3Hi8BQ02P495fh35c3DBKKVgzy5tkMLFwKQGEWkOv37qvhcQGphdv047eIKcO%2Ff8X%2FmZjfM%2FGJM7CKGjKwcquANLxhYPiXIXxxmztUKSbgNXUW5tB3mqgcMe2TY%2BuXyW71H1BDgY6AgQEAC35v3JCnE5EAAAAASUVORK5CYII%3D)
[![HitCount](http://hits.dwyl.com/jianastrero/compose-permissions.svg)](http://hits.dwyl.com/jianastrero/compose-permissions)
[![GitHub license](https://img.shields.io/github/license/jianastrero/compose-permissions.svg)](LICENSE)

## Overview

**"Compose Permissions"** is an Android library tailored to simplify the process of managing permissions within Jetpack Compose projects. Say goodbye to the verbosity of permission requests and checks, and welcome a cleaner, more efficient way to handle user permissions.

## Demo

![Compose Permissions Demo GIF](assets/demo.gif)

## Installation

To integrate "Compose Permissions" into your project, just add the following dependency to your app's `build.gradle` file:

```kotlin
implementation("dev.jianastrero.compose-permissions:compose-permissions:1.0.0")
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

## Documentation

### value

- **Description:** Returns the value of the permission.
- **Type:** `String`
- **Usage:**

```kotlin
cameraPermission.value
```

### isGranted

- **Description:** Returns true if the permission is granted, false otherwise.
- **Type:** `Boolean`
- **Usage:**

```kotlin
if (!cameraPermission.isGranted) {
    cameraPermission.request()
}
```

### shouldShowRationale

- **Description:** Returns true if the permission should show rationale, false otherwise.
- **Type:** `Boolean`
- **Usage:**

```kotlin
if (cameraPermission.shouldShowRationale) {
    // Show rationale
}
```

### request()

- **Description:** Requests the permission.
- **Type:** `Unit`
- **Usage:**

```kotlin
cameraPermission.request()
```

## 🚀 What's Next?

- [ ]  Add support for multiple permissions
- [ ]  Add support for iOS and other platforms (Kotlin Multiplatform)

## ✒️ Contributing

We value all forms of contributions from the community, including bug reports, issues, pull requests, documentation updates, and other improvements. Please respect our guidelines and community when making a contribution.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
