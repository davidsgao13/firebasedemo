package com.example.firebasesignin.domain.repository

import android.content.Context
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * The AuthRepository interface abstracts authentication actions, such as signing in with Google.
 * It defines what authentication capabilities are available, but not how they are implemented.
 * Since the domain layer (which contains business logic and use cases) should not depend directly
 * on the data layer (which contains the implementation details of data sources like Firebase), we
 * define an AuthRepository interface in the domain layer to create a boundary between the domain
 * and data layers. This boundary lets us change the implementation in the future
 * (e.g., switch from Firebase to another auth provider) without impacting the domain layer.
 */
interface AuthRepository {
    suspend fun googleSignIn(context: Context): Flow<Result<AuthResult>>

    /**
     * No asynchronous work occurs in logout() because it is calling the method firebase.signOut(),
     * which is a synchronous operation. Since no I/O or long-running tasks are involved, suspend
     * isn’t necessary, as suspend is typically reserved for functions that need to execute
     * asynchronously.
     */
    fun logout()
    fun getSignedInUser(): FirebaseUser?
    val currentUser: StateFlow<FirebaseUser?>
}