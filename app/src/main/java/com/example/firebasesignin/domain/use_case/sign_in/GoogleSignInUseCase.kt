package com.example.firebasesignin.domain.use_case.sign_in

import android.content.Context
import com.example.firebasesignin.domain.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * The SignInWithGoogleUseCase is a use case that orchestrates the Google Sign-In process.
 * It interacts with the AuthRepository to perform the actual sign-in operation, but it’s the only
 * layer of the domain that the ViewModel needs to know about.
 *
 * Also, by injecting the repository directly into the use case here, we don't need to add another
 * @Provides in the AuthModule for the use case. This is because Hilt automatically constructs it
 * without needing a @Provides annotation, since it can resolve dependencies automatically in a
 * chain. GoogleSignInUseCase is constructed automatically by Hilt because it uses @Inject on
 * its constructor, and any class calling GoogleSignInUseCase will receive it automatically
 * without needing a @Provides in the AppModule.
 */
class GoogleSignInUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(context: Context): Flow<Result<AuthResult>> {
        return authRepository.googleSignIn(context)
    }
}