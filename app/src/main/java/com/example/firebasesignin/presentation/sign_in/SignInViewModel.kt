package com.example.firebasesignin.presentation.sign_in

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasesignin.domain.repository.AuthRepository
import com.example.firebasesignin.domain.use_case.authentication.ObserveAuthStateUseCase
import com.example.firebasesignin.domain.use_case.logout.LogoutUseCase
import com.example.firebasesignin.domain.use_case.sign_in.GoogleSignInUseCase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The LoginViewModel manages the UI state and interaction logic for the login screen.
 * It calls the SignInWithGoogleUseCase to perform the sign-in operation but does not
 * contain the actual sign-in logic itself.
 */

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val signInWithGoogleUseCase: GoogleSignInUseCase,
    private val logoutUseCase: LogoutUseCase

) : ViewModel() {

    private val _uiState = MutableStateFlow<SignInUiState>(SignInUiState.Loading)
    val uiState: StateFlow<SignInUiState> = _uiState

    init {
        // Observe the authentication state
        viewModelScope.launch {
            observeAuthStateUseCase().collect { user ->
                _uiState.value = if (user != null) {
                    SignInUiState.Success(user)
                } else {
                    SignInUiState.LoggedOut
                }
            }
        }
    }

    fun handleGoogleSignIn(context: Context) {
        viewModelScope.launch {
            _uiState.value = SignInUiState.Loading
            signInWithGoogleUseCase(context).collect { result ->
                result.fold(
                    onSuccess = { authResult ->
                        val user = authResult.user
                        _uiState.value = SignInUiState.Success(user)
                    },
                    onFailure = { e ->
                        _uiState.value =
                            SignInUiState.Error("Authentication failed. Please try again.")
                    }
                )
            }
        }
    }

    fun logout() {
        logoutUseCase()
        _uiState.value = SignInUiState.LoggedOut
    }
}