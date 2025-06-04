package com.yandex.mobile_school.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.yandex.mobile_school.example.data.model.User
import com.yandex.mobile_school.example.di.module.ApplicationContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing local authentication state.
 * Handles saving/loading current user session and authentication tokens.
 */
@Singleton
class LocalAuthRepository @Inject constructor(
  @ApplicationContext private val context: Context
) {

  private val sharedPreferences: SharedPreferences by lazy {
    context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
  }

  /**
   * Save user session after successful login
   */
  fun saveUserSession(user: User, token: String) {
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

    sharedPreferences.edit().apply {
      putString(KEY_USER_DATA, userJson.toString())
      putString(KEY_AUTH_TOKEN, token)
      putBoolean(KEY_IS_LOGGED_IN, true)
      apply()
    }
  }

  /**
   * Get saved user from local storage
   * @return User if logged in, null otherwise
   */
  fun getSavedUser(): User? {
    if (!isUserLoggedIn()) return null

    val userDataString = sharedPreferences.getString(KEY_USER_DATA, null) ?: return null

    return try {
      val userJson = JSONObject(userDataString)
      User(
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
    } catch (e: Exception) {
      // If parsing fails, clear corrupted data
      clearUserSession()
      null
    }
  }

  /**
   * Get saved authentication token
   */
  fun getSavedToken(): String? {
    return if (isUserLoggedIn()) {
      sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    } else null
  }

  /**
   * Check if user is currently logged in
   */
  fun isUserLoggedIn(): Boolean {
    return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
  }

  /**
   * Clear user session (logout)
   */
  fun clearUserSession() {
    sharedPreferences.edit().apply {
      remove(KEY_USER_DATA)
      remove(KEY_AUTH_TOKEN)
      putBoolean(KEY_IS_LOGGED_IN, false)
      apply()
    }
  }

  /**
   * Update user data in local storage
   */
  fun updateUserData(user: User) {
    if (!isUserLoggedIn()) return

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

    sharedPreferences.edit().apply {
      putString(KEY_USER_DATA, userJson.toString())
      apply()
    }
  }

  companion object {
    private const val PREF_NAME = "local_auth_prefs"
    private const val KEY_USER_DATA = "user_data"
    private const val KEY_AUTH_TOKEN = "auth_token"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
  }
}
