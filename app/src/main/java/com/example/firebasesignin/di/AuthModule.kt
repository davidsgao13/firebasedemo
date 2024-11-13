package com.example.firebasesignin.di

import com.example.firebasesignin.data.repository.AuthRepositoryImpl
import com.example.firebasesignin.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Dependency injection, necessary for ViewModels to interact with the domain layer.
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl()
}