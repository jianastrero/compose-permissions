package dev.jianastrero.compose_permissions.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jianastrero.compose_permissions.composePermission

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainContent()
        }
    }
}

@Composable
private fun MainContent() {
    val cameraPermission = composePermission(permission = android.Manifest.permission.CAMERA)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Granted: ${cameraPermission.isGranted}",
            color = if (cameraPermission.isGranted) Color.Green else Color.Red,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black
        )
        Button(
            onClick = {
                if (cameraPermission.isGranted) {
                    /* Do Something */
                } else {
                    cameraPermission.request()
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
    MainContent()
}
