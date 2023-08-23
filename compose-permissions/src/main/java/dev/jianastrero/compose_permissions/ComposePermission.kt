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

/**
 * Represents a ComposePermission object that provides functionality for managing a permission status.
 *
 * @constructor Creates a ComposePermission object with a default permission status of Denied.
 */
class ComposePermission internal constructor() {
    internal var _value by mutableStateOf(PermissionStatus.Denied)
    internal var _shouldShowRationale by mutableStateOf(false)
    internal var launch by mutableStateOf<() -> Unit>({})

    /**
     * Represents the status of a permission.
     *
     * The permission status indicates whether the app was granted the permission or not.
     * This is a read-only property.
     *
     * @return The status of a permission.
     */
    val value: PermissionStatus
        get() = _value

    /**
     * Determines if the permission is granted.
     *
     * @return `true` if the permission is granted, `false` otherwise.
     */
    val isGranted: Boolean
        get() = _value == PermissionStatus.Granted

    /**
     * Indicates whether rationale should be shown.
     *
     * @return `true` if rationale should be shown, `false` otherwise.
     */
    val shouldShowRationale: Boolean
        get() = _shouldShowRationale

    /**
     * Sends a request to perform an action. This method should be called when it is required to make a request for an action
     * without any additional parameters.
     *
     * @return Unit
     */
    fun request() {
        launch()
    }
}


@Composable
fun composePermission(permission: String): ComposePermission {
    val activity = LocalContext.current as Activity
    val composePermission = remember {
        ComposePermission()
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        composePermission._value = if (isGranted) PermissionStatus.Granted else PermissionStatus.Denied
    }

    composePermission.launch = {
        launcher.launch(permission)
    }

    DisposableEffect(activity) {
        composePermission._value = when (ContextCompat.checkSelfPermission(activity, permission)) {
            PackageManager.PERMISSION_GRANTED -> PermissionStatus.Granted
            else -> PermissionStatus.Denied
        }
        composePermission._shouldShowRationale = activity.shouldShowRequestPermissionRationale(permission)
        onDispose { /* Do Nothing */ }
    }

    return composePermission
}
