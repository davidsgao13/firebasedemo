package com.example.firebasesignin.presentation.sign_in

import com.google.firebase.auth.FirebaseUser

sealed class SignInUiState {
    data object Loading : SignInUiState()
    data class Success(val user: FirebaseUser?) : SignInUiState()
    data class Error(val message: String) : SignInUiState()
    data object LoggedOut : SignInUiState()
}