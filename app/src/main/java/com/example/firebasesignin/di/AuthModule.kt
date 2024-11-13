package com.example.firebasesignin.di

import androidx.test.espresso.core.internal.deps.dagger.Module
import androidx.test.espresso.core.internal.deps.dagger.Provides
import com.example.firebasesignin.data.repository.AuthRepositoryImpl
import com.example.firebasesignin.domain.repository.AuthRepository

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl()
}