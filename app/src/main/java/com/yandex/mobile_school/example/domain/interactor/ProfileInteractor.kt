package com.yandex.mobile_school.example.domain.interactor

import com.yandex.mobile_school.example.data.model.User
import com.yandex.mobile_school.example.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
  private val userRepository: UserRepository,
  private val localAuthInteractor: AuthInteractor,
) {

  /**
   * Gets current user profile
   */
  fun getUserProfile(): Flow<User> {
    return userRepository.getUserProfile()
  }

  /**
   * Logs out current user
   */
  suspend fun logout() {
    userRepository.logout()
    localAuthInteractor.logout()
  }

  /**
   * Checks if user is currently logged in
   */
  fun isUserLoggedIn(): Boolean {
    val token = userRepository.getAuthToken()
    val userId = userRepository.getUserId()

    return !token.isNullOrBlank() && !userId.isNullOrBlank()
  }

  /**
   * Gets current user ID
   */
  fun getCurrentUserId(): String? {
    return userRepository.getUserId()
  }

  /**
   * Validates user ID according to business rules
   */
  private fun validateUserId(userId: String) {
    if (userId.isBlank()) {
      throw IllegalArgumentException("User ID cannot be empty")
    }

    // Additional validation can be added here
    if (userId.length < 3) {
      throw IllegalArgumentException("Invalid user ID format")
    }
  }

  /**
   * Clears user session data
   */
  private fun clearUserSession() {
    // Here we could add business logic for:
    // - Clearing cached data
    // - Sending analytics events
    // - Clearing temporary files
    // - etc.
  }
}
