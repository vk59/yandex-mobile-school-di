package com.yandex.mobile_school.example.data.repository

import android.content.Context

/**
 * Interface for the SettingsRepository.
 * This will be used for dependency injection in the future.
 */
interface SettingsRepository {
    fun setDarkMode(context: Context, enabled: Boolean)
    fun isDarkModeEnabled(context: Context): Boolean
    fun setNotificationsEnabled(context: Context, enabled: Boolean)
    fun areNotificationsEnabled(context: Context): Boolean
    fun setLanguage(context: Context, language: String)
    fun getLanguage(context: Context): String
    fun resetAllSettings(context: Context)
}
