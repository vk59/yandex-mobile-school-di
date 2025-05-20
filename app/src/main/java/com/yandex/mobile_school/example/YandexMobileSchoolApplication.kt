package com.yandex.mobile_school.example

import android.app.Application
import com.yandex.mobile_school.example.data.repository.MockUserRepository
import com.yandex.mobile_school.example.data.repository.SettingsRepositoryImpl
import com.yandex.mobile_school.example.data.repository.UserRepository

class YandexMobileSchoolApplication : Application() {

  private val useMockRepositories = true

  override fun onCreate() {
    super.onCreate()

    if (useMockRepositories) {
      // Initialize mock data
      MockUserRepository.loadAllUsersFromPrefs(this)
    }
  }

  override fun onTerminate() {
    super.onTerminate()

    // Save mock data when the app is terminated
    if (useMockRepositories) {
      MockUserRepository.saveAllUsersToPrefs(this)
    }
  }

  companion object {

    fun getUserRepository(): UserRepository {
      return MockUserRepository
    }

    fun getSettingsRepository() = SettingsRepositoryImpl.getInstance()
  }
}
