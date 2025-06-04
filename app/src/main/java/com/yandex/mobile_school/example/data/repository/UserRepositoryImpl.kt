package com.yandex.mobile_school.example.data.repository

import android.content.Context
import android.util.Log
import com.yandex.mobile_school.example.data.api.RetrofitClient
import com.yandex.mobile_school.example.data.model.LoginRequest
import com.yandex.mobile_school.example.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
  private val context: Context
) : UserRepository {

  private val apiService = RetrofitClient.apiService

  private var currentUser: User? = null
  private var authToken: String? = null

  override suspend fun login(username: String, password: String): Flow<User> = flow {
    try {
      val loginRequest = LoginRequest(username, password)
      val loginResponse = apiService.login(loginRequest)

      saveUserSession(context, loginResponse.token, loginResponse.user.id)
      currentUser = loginResponse.user
      authToken = loginResponse.token

      emit(loginResponse.user)
    } catch (e: Exception) {
      Log.e(TAG, "Login error", e)
      throw e
    }
  }.flowOn(Dispatchers.IO)

  override fun getUserProfile(): Flow<User> = flow {
    if (currentUser != null) {
      emit(currentUser!!)
      return@flow
    }

    try {
      val user = apiService.getUserProfile()
      currentUser = user
      emit(user)
    } catch (e: Exception) {
      Log.e(TAG, "Get user profile error", e)
      throw e
    }
  }.flowOn(Dispatchers.IO)

  override fun getAuthToken(): String? {
    if (authToken != null) {
      return authToken
    }

    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    authToken = sharedPreferences.getString(KEY_TOKEN, null)
    return authToken
  }

  override fun getUserId(): String? {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getString(KEY_USER_ID, null)
  }

  override suspend fun logout() {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
    currentUser = null
    authToken = null
  }

  private fun saveUserSession(context: Context, token: String, userId: String) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit().apply {
      putString(KEY_TOKEN, token)
      putString(KEY_USER_ID, userId)
      apply()
    }
  }

  companion object {

    private const val TAG = "UserRepository"
    private const val PREF_NAME = "user_prefs"
    private const val KEY_TOKEN = "token"
    private const val KEY_USER_ID = "user_id"
  }
}
