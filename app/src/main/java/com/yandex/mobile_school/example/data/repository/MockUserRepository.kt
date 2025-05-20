package com.yandex.mobile_school.example.data.repository

import android.content.Context
import android.util.Log
import com.yandex.mobile_school.example.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import kotlin.random.Random


private const val PREF_NAME = "mock_user_prefs"
private const val MOCK_USERS_PREF = "mock_users_database"
private const val KEY_TOKEN = "token"
private const val KEY_USER_ID = "user_id"
private const val KEY_MOCK_USERS = "mock_users"
private const val KEY_CREDENTIALS = "credentials"
private const val KEY_USER_ID_MAP = "user_id_map"
private const val MOCK_NETWORK_DELAY = 500L // 500ms delay to simulate network

/**
 * A mock implementation of UserRepository that stores data locally.
 * Uses coroutines and flows for asynchronous operations.
 */
object MockUserRepository : UserRepository {

  private val TAG = "MockUserRepository"

  // In-memory cache of the current user
  private var currentUser: User? = null
  private var authToken: String? = null

  // Mock users database
  private val mockUsers = mutableMapOf<String, User>()

  // Credentials map (username -> password)
  private val credentials = mutableMapOf<String, String>()

  // Username to userId map
  private val userIdMap = mutableMapOf<String, String>()

  init {
    // Initialize with some mock users
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
  override suspend fun login(context: Context, username: String, password: String): Flow<User> = flow {
    delay(MOCK_NETWORK_DELAY)

    if (credentials.containsKey(username) && credentials[username] == password) {
      val userId = userIdMap[username]
      val user = mockUsers[userId]

      if (user != null) {
        // Generate a mock token
        val token = "mock_token_${UUID.randomUUID()}"

        // Save to SharedPreferences
        saveUserSession(context, token, user.id)

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

    if (currentUser != null) {
      Log.d(TAG, "Returning user profile from cache")
      emit(currentUser!!)
    } else {
      Log.e(TAG, "No user is logged in")
      throw Exception("No user is logged in")
    }
  }.flowOn(Dispatchers.IO)

  /**
   * Get details for a specific user
   * @return Flow that emits the user on success or throws an exception on failure
   */
  override fun getUserDetails(userId: String): Flow<User> = flow {
    // Simulate network delay
    delay(MOCK_NETWORK_DELAY)

    val user = mockUsers[userId]
    if (user != null) {
      Log.d(TAG, "Retrieved user details for ID: $userId")
      emit(user)
    } else {
      Log.e(TAG, "User not found with ID: $userId")
      throw Exception("User not found")
    }
  }.flowOn(Dispatchers.IO)

  /**
   * Get the authentication token
   * @return The auth token or null if not logged in
   */
  override fun getAuthToken(context: Context): String? {
    if (authToken != null) {
      return authToken
    }

    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    authToken = sharedPreferences.getString(KEY_TOKEN, null)
    return authToken
  }

  /**
   * Get the current user's ID
   * @return The user ID or null if not logged in
   */
  override fun getUserId(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getString(KEY_USER_ID, null)
  }

  /**
   * Logout the current user
   */
  override suspend fun logout(context: Context) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
    currentUser = null
    authToken = null
    Log.d(TAG, "User logged out")
  }

  private fun saveUserSession(context: Context, token: String, userId: String) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit().apply {
      putString(KEY_TOKEN, token)
      putString(KEY_USER_ID, userId)
      apply()
    }
  }

  /**
   * Save all mock users to SharedPreferences.
   * This is useful for persisting the mock database between app launches.
   */
  fun saveAllUsersToPrefs(context: Context) {
    val sharedPreferences = context.getSharedPreferences(MOCK_USERS_PREF, Context.MODE_PRIVATE)
    val usersArray = JSONArray()

    mockUsers.values.forEach { user ->
      val userJson = JSONObject().apply {
        put("id", user.id)
        put("username", user.username)
        put("email", user.email)
        put("firstName", user.firstName)
        put("lastName", user.lastName)
        put("avatar", user.avatar)
        put("phone", user.phone)
        put("address", user.address)
        put("bio", user.bio)
      }
      usersArray.put(userJson)
    }

    val credentialsJson = JSONObject()
    credentials.forEach { (username, password) ->
      credentialsJson.put(username, password)
    }

    val userIdMapJson = JSONObject()
    userIdMap.forEach { (username, userId) ->
      userIdMapJson.put(username, userId)
    }

    sharedPreferences.edit().apply {
      putString(KEY_MOCK_USERS, usersArray.toString())
      putString(KEY_CREDENTIALS, credentialsJson.toString())
      putString(KEY_USER_ID_MAP, userIdMapJson.toString())
      apply()
    }
  }

  /**
   * Load all mock users from SharedPreferences.
   * This is useful for restoring the mock database between app launches.
   */
  fun loadAllUsersFromPrefs(context: Context) {
    val sharedPreferences = context.getSharedPreferences(MOCK_USERS_PREF, Context.MODE_PRIVATE)

    val usersString = sharedPreferences.getString(KEY_MOCK_USERS, null) ?: return
    val credentialsString = sharedPreferences.getString(KEY_CREDENTIALS, null) ?: return
    val userIdMapString = sharedPreferences.getString(KEY_USER_ID_MAP, null) ?: return

    try {
      // Clear existing data
      mockUsers.clear()
      credentials.clear()
      userIdMap.clear()

      // Load users
      val usersArray = JSONArray(usersString)
      for (i in 0 until usersArray.length()) {
        val userJson = usersArray.getJSONObject(i)
        val user = User(
          id = userJson.getString("id"),
          username = userJson.getString("username"),
          email = userJson.getString("email"),
          firstName = userJson.getString("firstName"),
          lastName = userJson.getString("lastName"),
          avatar = userJson.getString("avatar"),
          phone = userJson.getString("phone"),
          address = userJson.getString("address"),
          bio = userJson.getString("bio")
        )
        mockUsers[user.id] = user
      }

      // Load credentials
      val credentialsJson = JSONObject(credentialsString)
      credentialsJson.keys().forEach { username ->
        credentials[username] = credentialsJson.getString(username)
      }

      // Load userIdMap
      val userIdMapJson = JSONObject(userIdMapString)
      userIdMapJson.keys().forEach { username ->
        userIdMap[username] = userIdMapJson.getString(username)
      }

      Log.d(TAG, "Loaded ${mockUsers.size} users from SharedPreferences")
    } catch (e: Exception) {
      Log.e(TAG, "Error loading mock users from SharedPreferences", e)
    }
  }

  /**
   * Add a new user to the mock database.
   * This is useful for testing registration functionality.
   */
  fun addUser(
    username: String,
    password: String,
    email: String,
    firstName: String,
    lastName: String,
    context: Context? = null
  ): User {
    // Check if username already exists
    if (userIdMap.containsKey(username)) {
      throw IllegalArgumentException("Username already exists")
    }

    // Create a new user
    val userId = UUID.randomUUID().toString()
    val avatarId = Random.nextInt(1, 99)
    val gender = if (Random.nextBoolean()) "men" else "women"

    val user = User(
      id = userId,
      username = username,
      email = email,
      firstName = firstName,
      lastName = lastName,
      avatar = "https://randomuser.me/api/portraits/$gender/$avatarId.jpg",
      phone = "+${Random.nextLong(1000000000, 9999999999)}",
      address = "${Random.nextInt(1, 999)} Example St, City",
      bio = "New user"
    )

    // Add to mock database
    mockUsers[userId] = user
    credentials[username] = password
    userIdMap[username] = userId

    // Save to SharedPreferences if context is provided
    if (context != null) {
      saveAllUsersToPrefs(context)
    }

    return user
  }
}
