package com.yandex.mobile_school.example.ui.compose

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.mobile_school.example.YandexMobileSchoolApplication
import com.yandex.mobile_school.example.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Compose Settings screen.
 * Uses StateFlow for reactive state management.
 */
class ComposeSettingsViewModel(application: Application) : AndroidViewModel(application) {

    // Get the repository from the Application companion object
    private val settingsRepository: SettingsRepository = YandexMobileSchoolApplication.getSettingsRepository()
    
    // Settings state
    private val _darkModeEnabled = MutableStateFlow(false)
    val darkModeEnabled: StateFlow<Boolean> = _darkModeEnabled
    
    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled
    
    private val _language = MutableStateFlow("en")
    val language: StateFlow<String> = _language
    
    // Language data
    private val _languages = MutableStateFlow(listOf("English", "Spanish", "French", "German", "Russian"))
    val languages: StateFlow<List<String>> = _languages
    
    private val _languageCodes = MutableStateFlow(listOf("en", "es", "fr", "de", "ru"))
    val languageCodes: StateFlow<List<String>> = _languageCodes
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        val context = getApplication<Application>()
        _darkModeEnabled.value = settingsRepository.isDarkModeEnabled(context)
        _notificationsEnabled.value = settingsRepository.areNotificationsEnabled(context)
        _language.value = settingsRepository.getLanguage(context)
    }
    
    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkMode(getApplication(), enabled)
            _darkModeEnabled.value = enabled
        }
    }
    
    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotificationsEnabled(getApplication(), enabled)
            _notificationsEnabled.value = enabled
        }
    }
    
    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            settingsRepository.setLanguage(getApplication(), languageCode)
            _language.value = languageCode
        }
    }
    
    fun resetAllSettings() {
        viewModelScope.launch {
            settingsRepository.resetAllSettings(getApplication())
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
