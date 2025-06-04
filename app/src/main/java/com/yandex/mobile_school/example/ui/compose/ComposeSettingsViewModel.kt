package com.yandex.mobile_school.example.ui.compose

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.mobile_school.example.data.repository.SettingsRepository
import com.yandex.mobile_school.example.di.module.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Compose Settings screen.
 * Uses StateFlow for reactive state management.
 */
class ComposeSettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    private val _darkModeEnabled = MutableStateFlow(false)
    val darkModeEnabled: StateFlow<Boolean> = _darkModeEnabled
    
    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled
    
    private val _language = MutableStateFlow("en")
    val language: StateFlow<String> = _language
    
    private val _languages = MutableStateFlow(listOf("English", "Spanish", "French", "German", "Russian"))
    val languages: StateFlow<List<String>> = _languages
    
    private val _languageCodes = MutableStateFlow(listOf("en", "es", "fr", "de", "ru"))
    val languageCodes: StateFlow<List<String>> = _languageCodes
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        _darkModeEnabled.value = settingsRepository.isDarkModeEnabled(context)
        _notificationsEnabled.value = settingsRepository.areNotificationsEnabled(context)
        _language.value = settingsRepository.getLanguage(context)
    }
    
    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkMode(context, enabled)
            _darkModeEnabled.value = enabled
        }
    }
    
    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotificationsEnabled(context, enabled)
            _notificationsEnabled.value = enabled
        }
    }
    
    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            settingsRepository.setLanguage(context, languageCode)
            _language.value = languageCode
        }
    }
    
    fun resetAllSettings() {
        viewModelScope.launch {
            settingsRepository.resetAllSettings(context)
            loadSettings()
        }
    }
    
    fun getLanguageIndex(languageCode: String): Int {
        return _languageCodes.value.indexOf(languageCode).takeIf { it >= 0 } ?: 0
    }
    
    fun getLanguageName(languageCode: String): String {
        val index = getLanguageIndex(languageCode)
        return _languages.value[index]
    }
}
