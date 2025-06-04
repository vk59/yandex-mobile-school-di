package com.yandex.mobile_school.example.domain.interactor

import android.content.Context
import com.yandex.mobile_school.example.data.repository.SettingsRepository
import com.yandex.mobile_school.example.di.module.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

class SettingsInteractor @Inject constructor(
    private val settingsRepository: SettingsRepository,
    @ApplicationContext private val context: Context
) {

    /**
     * Gets current dark mode setting
     */
    fun isDarkModeEnabled(): Boolean {
        return settingsRepository.isDarkModeEnabled(context)
    }

    /**
     * Sets dark mode with validation
     */
    fun setDarkMode(enabled: Boolean) {
        // Business logic: validate and apply dark mode
        validateDarkModeChange(enabled)
        settingsRepository.setDarkMode(context, enabled)
    }

    /**
     * Gets current notifications setting
     */
    fun areNotificationsEnabled(): Boolean {
        return settingsRepository.areNotificationsEnabled(context)
    }

    /**
     * Sets notifications with validation
     */
    fun setNotificationsEnabled(enabled: Boolean) {
        // Business logic: validate notifications change
        validateNotificationsChange(enabled)
        settingsRepository.setNotificationsEnabled(context, enabled)
    }

    /**
     * Gets current language setting
     */
    fun getLanguage(): String {
        return settingsRepository.getLanguage(context)
    }

    /**
     * Sets language with validation
     */
    fun setLanguage(languageCode: String) {
        // Business logic: validate language code
        validateLanguageCode(languageCode)
        settingsRepository.setLanguage(context, languageCode)
    }

    /**
     * Resets all settings to default values
     */
    fun resetAllSettings() {
        // Business logic: confirm reset operation
        validateResetOperation()
        settingsRepository.resetAllSettings(context)
    }

    /**
     * Gets available languages
     */
    fun getAvailableLanguages(): List<String> {
        return listOf("English", "Spanish", "French", "German", "Russian")
    }

    /**
     * Gets available language codes
     */
    fun getAvailableLanguageCodes(): List<String> {
        return listOf("en", "es", "fr", "de", "ru")
    }

    /**
     * Gets language name by code
     */
    fun getLanguageName(languageCode: String): String {
        val codes = getAvailableLanguageCodes()
        val names = getAvailableLanguages()
        val index = codes.indexOf(languageCode)
        return if (index >= 0) names[index] else "English"
    }

    /**
     * Validates dark mode change according to business rules
     */
    private fun validateDarkModeChange(enabled: Boolean) {
        // Business logic: any restrictions on dark mode changes
        // For example, could check user permissions, device capabilities, etc.
    }

    /**
     * Validates notifications change according to business rules
     */
    private fun validateNotificationsChange(enabled: Boolean) {
        // Business logic: check if notifications are allowed
        // Could check system permissions, user account type, etc.
    }

    /**
     * Validates language code according to business rules
     */
    private fun validateLanguageCode(languageCode: String) {
        if (languageCode.isBlank()) {
            throw IllegalArgumentException("Language code cannot be empty")
        }
        
        val availableCodes = getAvailableLanguageCodes()
        if (!availableCodes.contains(languageCode)) {
            throw IllegalArgumentException("Unsupported language code: $languageCode")
        }
    }

    /**
     * Validates reset operation according to business rules
     */
    private fun validateResetOperation() {
        // Business logic: any restrictions on resetting settings
        // Could check user permissions, backup requirements, etc.
    }
}
