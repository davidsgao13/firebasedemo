package com.example.firebasesignin.domain.use_case.authentication

import com.example.firebasesignin.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): StateFlow<FirebaseUser?> = authRepository.currentUser
}