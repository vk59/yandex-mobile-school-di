package com.yandex.mobile_school.example.data.repository

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor() : SettingsRepository {

  override fun setDarkMode(context: Context, enabled: Boolean) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
  }

  override fun isDarkModeEnabled(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean(KEY_DARK_MODE, false)
  }

  override fun setNotificationsEnabled(context: Context, enabled: Boolean) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled).apply()
  }

  override fun areNotificationsEnabled(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
  }

  override fun setLanguage(context: Context, language: String) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit().putString(KEY_LANGUAGE, language).apply()
  }

  override fun getLanguage(context: Context): String {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getString(KEY_LANGUAGE, "en") ?: "en"
  }

  override fun resetAllSettings(context: Context) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
  }

  companion object {
    private const val PREF_NAME = "settings_prefs"
    private const val KEY_DARK_MODE = "dark_mode"
    private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
    private const val KEY_LANGUAGE = "language"
  }
}
