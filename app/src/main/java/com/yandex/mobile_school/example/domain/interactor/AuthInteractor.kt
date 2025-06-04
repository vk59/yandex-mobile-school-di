package com.yandex.mobile_school.example.domain.interactor

import com.yandex.mobile_school.example.data.model.User
import com.yandex.mobile_school.example.data.repository.LocalAuthRepository
import com.yandex.mobile_school.example.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Stateless interactor for authentication operations.
 * Contains business logic for login, logout, and session management.
 */
@Singleton
class AuthInteractor @Inject constructor(
    private val userRepository: UserRepository,
    private val localAuthRepository: LocalAuthRepository
) {

    /**
     * Perform login with username and password
     * Business logic: validates credentials and saves session locally
     */
    suspend fun login(username: String, password: String): Flow<User> {
        // Business logic: validate input
        validateLoginInput(username, password)

        return userRepository.login(username, password)
            .onEach { user ->
                // Business logic: save session after successful login
                val token = generateSessionToken(user)
                localAuthRepository.saveUserSession(user, token)
            }
            .catch { error ->
                // Business logic: handle login errors
                handleLoginError(error)
                throw error
            }
    }

    /**
     * Logout current user
     * Business logic: clears local session
     */
    fun logout() {
        localAuthRepository.clearUserSession()
    }

    /**
     * Check if user is currently logged in
     * Business logic: validates local session
     */
    fun isUserLoggedIn(): Boolean {
        return localAuthRepository.isUserLoggedIn()
    }

    /**
     * Get current logged in user
     * Business logic: returns saved user or null
     */
    fun getCurrentUser(): User? {
        return localAuthRepository.getSavedUser()
    }

    /**
     * Get current authentication token
     */
    fun getCurrentToken(): String? {
        return localAuthRepository.getSavedToken()
    }

    /**
     * Update current user data
     * Business logic: updates local storage
     */
    fun updateCurrentUser(user: User) {
        if (isUserLoggedIn()) {
            localAuthRepository.updateUserData(user)
        }
    }

    /**
     * Validate login input according to business rules
     */
    private fun validateLoginInput(username: String, password: String) {
        if (username.isBlank()) {
            throw IllegalArgumentException("Username cannot be empty")
        }
        
        if (password.isBlank()) {
            throw IllegalArgumentException("Password cannot be empty")
        }
        
        if (username.length < 3) {
            throw IllegalArgumentException("Username must be at least 3 characters long")
        }
        
        if (password.length < 6) {
            throw IllegalArgumentException("Password must be at least 6 characters long")
        }
    }

    /**
     * Generate session token for user
     * Business logic: creates unique token for session
     */
    private fun generateSessionToken(user: User): String {
        return "session_${user.id}_${System.currentTimeMillis()}"
    }

    /**
     * Handle login errors according to business rules
     */
    private fun handleLoginError(error: Throwable) {
        // Business logic for error handling:
        // - Log error for debugging
        // - Could track failed login attempts
        // - Could implement rate limiting
        // - Could show specific error messages
        
        android.util.Log.e("AuthInteractor", "Login failed", error)
    }
}
