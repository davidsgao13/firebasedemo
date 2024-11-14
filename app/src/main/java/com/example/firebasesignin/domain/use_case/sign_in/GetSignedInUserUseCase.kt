package com.example.firebasesignin.domain.use_case.sign_in

import com.example.firebasesignin.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetSignedInUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    // Expose current user as a flow for other parts of the app to observe
    val currentUser: StateFlow<FirebaseUser?> = authRepository.currentUser
}