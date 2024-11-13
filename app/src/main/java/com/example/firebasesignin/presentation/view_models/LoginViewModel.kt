package com.example.firebasesignin.presentation.view_models

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasesignin.domain.use_case.sign_in.GoogleSignInUseCase
import kotlinx.coroutines.launch

/**
 * The LoginViewModel manages the UI state and interaction logic for the login screen.
 * It calls the SignInWithGoogleUseCase to perform the sign-in operation but does not
 * contain the actual sign-in logic itself.
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInWithGoogleUseCase: GoogleSignInUseCase
) : ViewModel() {

    fun handleGoogleSignIn(context: Context) {
        viewModelScope.launch {
            signInWithGoogleUseCase(context).collect { result ->
                result.fold(
                    onSuccess = { /* Handle success */ },
                    onFailure = { e -> /* Handle error */ }
                )
            }
        }
    }
}