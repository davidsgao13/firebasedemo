package com.example.firebasesignin.presentation

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebasesignin.R
import com.example.firebasesignin.presentation.view_models.LoginViewModel
import com.example.firebasesignin.ui.theme.FirebasesigninTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Implementing the Firebase SDK within an Android Studio is somewhat tricky because it is
 * both related to data since it talks to the backend server, and is also UI related. So where
 * should it go?
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebasesigninTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val isDarkTheme = isSystemInDarkTheme()
    val context = LocalContext.current as Activity
    val onClick = { viewModel.handleGoogleSignIn(context) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Surface(
            shape = CircleShape,
            color = when (isDarkTheme) {
                true -> Color(0xFF131314)
                false -> Color(0xFFFFFFFF)
            },
            modifier = Modifier
                .height(50.dp)
                .width(260.dp)
                .clip(CircleShape)
                .border(
                    BorderStroke(
                        width = 1.dp,
                        color = when (isDarkTheme) {
                            true -> Color(0xFF8E918F)
                            false -> Color.Transparent
                        }
                    ),
                    shape = CircleShape
                )
                .clickable { onClick() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 5.dp)
            ) {
                Spacer(modifier = Modifier.width(14.dp))
                Image(
                    painterResource(id = R.drawable.android_light_rd_na),
                    contentDescription = null,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "Continue with Google")
                Spacer(modifier = Modifier.weight(1f))
            }
        }

    }
}