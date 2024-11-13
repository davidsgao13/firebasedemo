package com.example.firebasesignin.domain.use_case.sign_in

import android.content.Context
import com.example.firebasesignin.domain.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

/**
 * The SignInWithGoogleUseCase is a use case that orchestrates the Google Sign-In process.
 * It interacts with the AuthRepository to perform the actual sign-in operation, but it’s the only
 * layer of the domain that the ViewModel needs to know about.
 */
class GoogleSignInUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(context: Context): Flow<Result<AuthResult>> {
        return authRepository.googleSignIn(context)
    }
}