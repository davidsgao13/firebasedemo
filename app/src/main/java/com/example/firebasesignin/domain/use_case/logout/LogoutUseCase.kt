package com.example.firebasesignin.domain.use_case.logout

import com.example.firebasesignin.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke() {
        authRepository.logout()
    }
}