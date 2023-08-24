package dev.jianastrero.compose_permissions.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jianastrero.compose_permissions.ComposePermission
import dev.jianastrero.compose_permissions.composePermission
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
private fun MainScreen() {
    val snackBarHostState = remember { SnackbarHostState() }
    val singlePermission = composePermission(permission = android.Manifest.permission.CAMERA)
    val multiplePermissions = composePermission(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.READ_CONTACTS,
        android.Manifest.permission.READ_CALENDAR,
        android.Manifest.permission.READ_CALL_LOG,
        android.Manifest.permission.READ_SMS,
    )
    val coroutineScope = rememberCoroutineScope()
    var selectedComposePermission by remember { mutableStateOf(singlePermission) }

    Box(modifier = Modifier.fillMaxSize()) {
        MainContent(
            isCameraGranted = singlePermission.isGranted,
            areMultiplePermissionsGranted = multiplePermissions.isGranted,
            requestCameraPermission = {
                selectedComposePermission = singlePermission
                if (singlePermission.shouldShowRationale) {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            "Please allow the permission to use the camera.",
                            "Ok",
                        )
                    }
                } else {
                    singlePermission.request()
                }
            },
            requestMultiplePermissions = {
                selectedComposePermission = multiplePermissions
                if (multiplePermissions.shouldShowRationale) {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            "Please allow all of our permissions coz I'm too lazy to ask them one by one.",
                            "Ok",
                        )
                    }
                } else {
                    multiplePermissions.request()
                }
            }
        )
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) { snackbarData ->
            MySnackBar(snackbarData, selectedComposePermission)
        }
    }
}

@Composable
private fun MySnackBar(
    snackbarData: SnackbarData,
    selectedComposePermission: ComposePermission
) {
    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(Color.Black)
    ) {
        Text(
            text = snackbarData.visuals.message,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 12.dp, end = 12.dp)
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = {
                    snackbarData.dismiss()
                }
            ) {
                Text(
                    text = "Dismiss",
                    color = Color.Red
                )
            }
            TextButton(
                onClick = {
                    selectedComposePermission.request()
                    snackbarData.dismiss()
                }
            ) {
                Text(
                    text = snackbarData.visuals.actionLabel ?: "Ok",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun MainContent(
    isCameraGranted: Boolean,
    areMultiplePermissionsGranted: Boolean,
    requestCameraPermission: () -> Unit,
    requestMultiplePermissions: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        PermissionSection(
            isGranted = isCameraGranted,
            detailsText = "is Camera Granted: $isCameraGranted",
            requestText = "Request Camera Permission",
            requestPermission = requestCameraPermission,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(24.dp)
        )
        PermissionSection(
            isGranted = areMultiplePermissionsGranted,
            detailsText = "are Multiple Permissions Granted: $areMultiplePermissionsGranted",
            requestText = "Request Multiple Permission",
            requestPermission = requestMultiplePermissions,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.LightGray)
                .padding(24.dp)
        )
    }
}

@Composable
private fun PermissionSection(
    isGranted: Boolean,
    detailsText: String,
    requestText: String,
    requestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = detailsText,
            color = if (isGranted) Color.Green else Color.Red,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = {
                if (isGranted) {
                    /* Do Something */
                } else {
                    requestPermission()
                }
            },
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text(text = requestText)
        }
    }
}

@Preview
@Composable
private fun MainContentPreview() {
    MainContent(
        isCameraGranted = true,
        areMultiplePermissionsGranted = true,
        requestCameraPermission = { /* Do Nothing */ },
        requestMultiplePermissions = { /* Do Nothing */ },
    )
}
