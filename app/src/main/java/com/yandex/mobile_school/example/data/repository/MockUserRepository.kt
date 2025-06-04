package com.yandex.mobile_school.example.data.repository

import android.content.Context
import android.util.Log
import com.yandex.mobile_school.example.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

internal const val MOCK_NETWORK_DELAY = 500L // 500ms delay to simulate network

/**
 * A mock implementation of UserRepository that stores data locally.
 * Uses coroutines and flows for asynchronous operations.
 */
@Singleton
class MockUserRepository @Inject constructor(
  private val context: Context,
  private val localAuthRepository: LocalAuthRepository,
) : UserRepository {

  private val TAG = "MockUserRepository"

  @Volatile
  private var currentUser: User? = null

  @Volatile
  private var authToken: String? = null

  private val mockUsers = mutableMapOf<String, User>()

  private val credentials = mutableMapOf<String, String>()

  private val userIdMap = mutableMapOf<String, String>()

  init {
    addMockUser(
      "ivan",
      "password123",
      "john@example.com",
      "John",
      "Doe",
      "https://randomuser.me/api/portraits/men/1.jpg",
      "+1234567890",
      "123 Main St, City",
      "Software developer with 5 years of experience"
    )

    addMockUser(
      "jane_smith",
      "password456",
      "jane@example.com",
      "Jane",
      "Smith",
      "https://randomuser.me/api/portraits/women/2.jpg",
      "+0987654321",
      "456 Oak St, Town",
      "UX designer passionate about user-centered design"
    )

    addMockUser(
      "alex_wilson",
      "password789",
      "alex@example.com",
      "Alex",
      "Wilson",
      "https://randomuser.me/api/portraits/men/3.jpg",
      "+1122334455",
      "789 Pine St, Village",
      "Product manager with a background in marketing"
    )
  }

  private fun addMockUser(
    username: String,
    password: String,
    email: String,
    firstName: String,
    lastName: String,
    avatar: String,
    phone: String,
    address: String,
    bio: String
  ) {
    val userId = UUID.randomUUID().toString()
    val user = User(
      id = userId,
      username = username,
      email = email,
      firstName = firstName,
      lastName = lastName,
      avatar = avatar,
      phone = phone,
      address = address,
      bio = bio
    )

    mockUsers[userId] = user
    credentials[username] = password
    userIdMap[username] = userId
  }

  /**
   * Login with username and password
   * @return Flow that emits the user on success or throws an exception on failure
   */
  override suspend fun login(username: String, password: String): Flow<User> = flow {
    delay(MOCK_NETWORK_DELAY)

    if (credentials.containsKey(username) && credentials[username] == password) {
      val userId = userIdMap[username]
      val user = mockUsers[userId]

      if (user != null) {
        // Generate a mock token
        val token = "mock_token_${UUID.randomUUID()}"

        // Update in-memory cache
        currentUser = user
        authToken = token

        Log.d(TAG, "Login successful for user: $username")
        emit(user)
      } else {
        Log.e(TAG, "User data not found for username: $username")
        throw Exception("User data not found")
      }
    } else {
      Log.e(TAG, "Invalid credentials for username: $username")
      throw Exception("Invalid username or password")
    }
  }.flowOn(Dispatchers.IO)

  /**
   * Get the current user's profile
   * @return Flow that emits the user on success or throws an exception on failure
   */
  override fun getUserProfile(): Flow<User> = flow {
    if (currentUser != null) {
      emit(currentUser!!)
      return@flow
    }

    delay(MOCK_NETWORK_DELAY)
    currentUser = localAuthRepository.getSavedUser()

    if (currentUser != null) {
      Log.d(TAG, "Returning user profile from cache")
      emit(currentUser!!)
    } else {
      Log.e(TAG, "No user is logged in")
      throw Exception("No user is logged in")
    }
  }.flowOn(Dispatchers.IO)

  /**
   * Get the authentication token
   * @return The auth token or null if not logged in
   */
  override fun getAuthToken(): String? {
    if (authToken != null) {
      return authToken
    }

    authToken = localAuthRepository.getSavedToken()
    return authToken
  }

  /**
   * Get the current user's ID
   * @return The user ID or null if not logged in
   */
  override fun getUserId(): String? {
    return currentUser?.id
  }

  /**
   * Logout the current user
   */
  override suspend fun logout() {
    currentUser = null
    authToken = null
    Log.d(TAG, "User logged out")
  }
}
