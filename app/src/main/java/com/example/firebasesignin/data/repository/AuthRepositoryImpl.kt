package com.example.firebasesignin.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.firebasesignin.R
import com.example.firebasesignin.domain.repository.AuthRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID

/**
 * AuthRepositoryImpl is the implementation of AuthRepository and contains the actual logic for
 * Google Sign-In using Firebase. This is where we handle Firebase-specific logic, such as
 * initializing FirebaseAuth, managing credentials, and signing in with Google.
 * It belongs to the data layer, where it’s responsible for interacting with external services and
 * data sources. By implementing the AuthRepository interface, it allows the domain layer to request
 * authentication data without knowing or caring about the underlying implementation details.
 */
class AuthRepositoryImpl : AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _currentUser = MutableStateFlow(firebaseAuth.currentUser)
    override val currentUser: StateFlow<FirebaseUser?> get() = _currentUser

    private val authListener = FirebaseAuth.AuthStateListener { auth ->
        _currentUser.value = auth.currentUser
    }

    init {
        firebaseAuth.addAuthStateListener(authListener)
    }

    /**
     * @suspend as a keyword denotes that this function will be used asynchronously. It emits a
     * @Flow<Result<AuthResult>>, which is a reactive stream that can emit multiple values.
     * Those values will be captured and read by the collector as they come in.
     */
    override suspend fun googleSignIn(context: Context): Flow<Result<AuthResult>> {

        /**
         * Retrieves the singleton FirebaseAuth instance, which is required for authentication calls
         */


        /**
         * @callbackFlow is a Kotlin coroutine builder that bridges callback-based API calls with
         * a coroutine. This is a cold Flow, with elements that are sent to a SendChannel provided
         * to the builder's lambda via ProducerScope. It allows elements to be produced by code that
         * is either running concurrently or in a different context.
         * - Ensures thread safety and context preservation
         * - Can be used from any context (e.g. a callback-based API)
         */
        return callbackFlow {
            try {
                /**
                 * @CredentialManager is initialized to manage sign-in credentials. The manager
                 * handles secure, local storage and retrieval of user credentials
                 */
                /**
                 * @CredentialManager is initialized to manage sign-in credentials. The manager
                 * handles secure, local storage and retrieval of user credentials
                 */
                val credentialManager: CredentialManager = CredentialManager.create(context)

                /**
                 * Generates a nonce (random UUID) that will be hashed and used to ensure that
                 * the request is both secure and unique. The nonce is then hashed using SHA-256,
                 * protecting it from tampering
                 */
                val ranNonce: String = UUID.randomUUID().toString()
                val bytes: ByteArray = ranNonce.toByteArray()
                val md: MessageDigest = MessageDigest.getInstance("SHA-256")
                val digest: ByteArray = md.digest(bytes)
                val hashedNonce: String = digest.fold("") { str, it -> str + "%02x".format(it) }

                /**
                 * Creates a GetGoogleIdOption, specifying that any Google account can be used.
                 * sets an id, and attaches the nonce for security
                 */
                val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .setNonce(hashedNonce)
                    .build()

                // Request credentials
                val request: GetCredentialRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                // Get the credential result
                val result = credentialManager.getCredential(context, request)
                val credential = result.credential

                // Check if the received credential is a valid Google ID Token
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val authCredential =
                        GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                    val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                    trySend(Result.success(authResult))
                } else {
                    throw RuntimeException("Received an invalid credential type")
                }
            } catch (e: GetCredentialCancellationException) {
                trySend(Result.failure(Exception("Sign-in was canceled. Please try again.")))

            } catch (e: Exception) {
                trySend(Result.failure(e))
            }
            awaitClose { }
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
        _currentUser.value = null
    }

    override fun getSignedInUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}