package com.yandex.mobile_school.example.data.repository

import android.content.Context
import com.yandex.mobile_school.example.data.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the UserRepository.
 * This will be used for dependency injection in the future.
 * Uses coroutines and flows for asynchronous operations.
 */
interface UserRepository {
  /**
   * Login with username and password
   * @return Flow that emits the user on success or throws an exception on failure
   */
  suspend fun login(username: String, password: String): Flow<User>
  
  /**
   * Get the current user's profile
   * @return Flow that emits the user on success or throws an exception on failure
   */
  fun getUserProfile(): Flow<User>
  
  /**
   * Get the authentication token
   * @return The auth token or null if not logged in
   */
  fun getAuthToken(): String?
  
  /**
   * Get the current user's ID
   * @return The user ID or null if not logged in
   */
  fun getUserId(): String?
  
  /**
   * Logout the current user
   */
  suspend fun logout()
}
