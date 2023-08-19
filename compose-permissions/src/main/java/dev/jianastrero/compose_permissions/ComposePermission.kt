package dev.jianastrero.compose_permissions

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

class ComposePermission internal constructor() {
    internal var _value by mutableStateOf(PermissionStatus.Denied)
    internal var launch by mutableStateOf<() -> Unit>({})

    val value: PermissionStatus
        get() = _value

    val isGranted: Boolean
        get() = _value == PermissionStatus.Granted

    fun request() {
        launch()
    }
}

@Composable
fun composePermission(permission: String): ComposePermission {
    val context = LocalContext.current
    val composePermission = remember {
        ComposePermission()
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        composePermission._value = if (isGranted) PermissionStatus.Granted else PermissionStatus.Denied
    }

    composePermission.launch = {
        launcher.launch(permission)
    }

    DisposableEffect(context) {
        composePermission._value = when (ContextCompat.checkSelfPermission(context, permission)) {
            PackageManager.PERMISSION_GRANTED -> PermissionStatus.Granted
            else -> PermissionStatus.Denied
        }
        onDispose { /* Do Nothing */ }
    }

    return composePermission
}
