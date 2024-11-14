package com.example.firebasesignin.presentation.login

import android.app.Activity
import android.content.Context
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
import com.example.firebasesignin.presentation.sign_in.SignInViewModel

@Composable
fun LoginScreen(
    onSignInClick: (Context) -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val context = LocalContext.current as Activity

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
                .clickable { onSignInClick(context) }
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