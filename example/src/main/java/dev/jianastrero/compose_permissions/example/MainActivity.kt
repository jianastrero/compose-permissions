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
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val cameraPermission = composePermission(permission = android.Manifest.permission.CAMERA)
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        MainContent(
            isGranted = cameraPermission.isGranted,
            requestPermission = {
                if (cameraPermission.shouldShowRationale) {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            "Please allow the permission to use the camera.",
                            "Ok",
                        )
                    }
                } else {
                    cameraPermission.request()
                }
            }
        )
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) { snackbarData ->
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
                            cameraPermission.request()
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
    }
}

@Composable
private fun MainContent(
    isGranted: Boolean,
    requestPermission: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Granted: $isGranted",
            color = if (isGranted) Color.Green else Color.Red,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black
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
            Text(text = "Request Permission")
        }
    }
}

@Preview
@Composable
private fun MainContentPreview() {
    MainContent(isGranted = true, requestPermission = { /* Do Nothing */ })
}
