/*
 * MIT License
 *
 * Copyright (c) 2023 Jian James Astrero
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.jianastrero.compose_permissions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import dev.jianastrero.compose_permissions.enumeration.PermissionStatus
import dev.jianastrero.compose_permissions.model.PermissionItem

/**
 * The `ComposePermission` class represents a permission object that can be used
 * to request and check permissions in a Compose-based application.
 *
 * @property value A map of permissions and their corresponding status.
 * @property isGranted Indicates whether all permissions are granted.
 * @property shouldShowRationale Indicates whether permission rationale should be shown.
 */
class ComposePermission internal constructor() {
    internal var _permissions = emptyArray<String>()
    internal var _permissionItems by mutableStateOf(emptyMap<String, PermissionItem>())
    internal var launch by mutableStateOf<() -> Unit>({})

    /**
     * Getter for the `value` variable.
     *
     * Returns a Map of String keys to PermissionStatus values.
     * Each key corresponds to a permission item, and the corresponding value represents the status of that permission.
     *
     * @return A Map of String keys to PermissionStatus values.
     */
    val value: Map<String, PermissionStatus>
        get() = _permissionItems.mapValues { it.value.status }

    /**
     * Determines whether all permission items have been granted.
     *
     * @return `true` if all permission items are granted, `false` otherwise.
     */
    val isGranted: Boolean
        get() = _permissionItems.all { it.value.status == PermissionStatus.Granted }

    /**
     * Indicates whether the rationale should be shown for each permission item.
     *
     * @return `true` if the rationale should be shown for each permission item, `false` otherwise.
     */
    val shouldShowRationale: Boolean
        get() = _permissionItems.all { it.value.shouldShowRationale }

    /**
     * Makes a request by calling the [launch] method.
     */
    fun request() {
        launch()
    }

    /**
     * Retrieves the permission status for the given permissions.
     *
     * @param permissions the permissions to retrieve the status for
     * @return a map containing the permission as the key and its status as the value
     */
    operator fun get(vararg permissions: String): Map<String, PermissionStatus> {
        val x = if (permissions.isEmpty()) _permissions else permissions
        return _permissionItems
            .filterKeys { it in x }
            .mapValues { it.value.status }
    }

    /**
     * Retrieves the permission status for the given permissions.
     *
     * @param permissions the permissions to retrieve the status for
     * @return a map of permissions and their corresponding status
     */
    fun value(vararg permissions: String): Map<String, PermissionStatus> = get(*permissions)

    /**
     * Checks if all the given permissions are granted.
     *
     * @param permissions the permissions to check
     *
     * @return `true` if all the permissions are granted, `false` otherwise
     */
    fun isGranted(vararg permissions: String): Boolean = get(*permissions)
        .all { it.value == PermissionStatus.Granted }

    /**
     * Determines whether rationale should be shown for the given permissions.
     *
     * @param permissions the list of permissions to be checked
     * @return true if rationale should be shown, false otherwise
     */
    fun shouldShowRationale(vararg permissions: String): Boolean = get(*permissions)
        .all { it.value == PermissionStatus.Denied }
}

/**
 * Composes a permission with optional additional permissions.
 *
 * @param permission The main permission to be composed.
 * @param otherPermissions Additional permissions to be composed.
 * @return A ComposePermission object that can be used to handle permission requests.
 */
@Composable
fun composePermission(permission: String, vararg otherPermissions: String): ComposePermission {
    val activity = LocalContext.current as Activity
    val permissions = remember { arrayOf(permission, *otherPermissions) }
    val composePermission = remember { ComposePermission() }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        composePermission._permissionItems = map.mapValues {
            PermissionItem(
                status = if (it.value) PermissionStatus.Granted else PermissionStatus.Denied,
                shouldShowRationale = activity.shouldShowRequestPermissionRationale(it.key)
            )
        }
    }

    composePermission.launch = {
        launcher.launch(permissions)
    }

    DisposableEffect(activity) {
        composePermission._permissions = permissions
        composePermission._permissionItems = permissions.associateWith {
            PermissionItem(
                status = if (ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED) {
                    PermissionStatus.Granted
                } else {
                    PermissionStatus.Denied
                },
                shouldShowRationale = activity.shouldShowRequestPermissionRationale(it)
            )
        }
        onDispose { /* Do Nothing */ }
    }

    return composePermission
}
