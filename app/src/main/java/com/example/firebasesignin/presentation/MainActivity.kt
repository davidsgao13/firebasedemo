package com.example.firebasesignin.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebasesignin.domain.use_case.sign_in.GetSignedInUserUseCase
import com.example.firebasesignin.presentation.login.LoginScreen
import com.example.firebasesignin.presentation.sign_in.SignInScreen
import com.example.firebasesignin.presentation.sign_in.SignInUiState
import com.example.firebasesignin.presentation.sign_in.SignInViewModel
import com.example.firebasesignin.ui.theme.FirebasesigninTheme
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var getSignedInUserUseCase: GetSignedInUserUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebasesigninTheme {
                val navController = rememberNavController()
                val viewModel: SignInViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsState()

                // Observe currentUser flow from GetSignedInUserUseCase
                val user by getSignedInUserUseCase.currentUser.collectAsState(initial = null)

                // Unified LaunchedEffect for dynamic navigation
                LaunchedEffect(user, uiState) {
                    when {
                        user != null -> {
                            navController.navigate("signedIn") {
                                popUpTo("login") { inclusive = true }
                            }
                        }

                        uiState is SignInUiState.Loading -> {
                            navController.navigate("login") {
                                popUpTo("signedIn") { inclusive = true }
                            }
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(onSignInClick = { context ->
                                viewModel.handleGoogleSignIn(context)
                            })
                        }
                        composable("signedIn") {
                            when (uiState) {
                                is SignInUiState.Success -> {
                                    if (user != null) {
                                        SignInScreen(
                                            user = user as FirebaseUser,
                                            onLogoutClick = { viewModel.logout() })
                                    }
                                }

                                is SignInUiState.Error -> {
                                    Text(
                                        text = (uiState as SignInUiState.Error).message,
                                        color = Color.Red,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                                is SignInUiState.Loading -> {
                                    Column(modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center) {
                                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                                    }
                                }

                                else -> {
                                    LaunchedEffect(Unit) {
                                        navController.navigate("login") {
                                            popUpTo("signedIn") { inclusive = true }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}