package com.yandex.mobile_school.example.ui.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.mobile_school.example.YandexMobileSchoolApplication
import com.yandex.mobile_school.example.data.model.User
import com.yandex.mobile_school.example.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

  private val userRepository: UserRepository = YandexMobileSchoolApplication.getUserRepository()

  private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
  val profileState: StateFlow<ProfileState> = _profileState

  init {
    loadUserProfile()
  }

  fun loadUserProfile() {
    _profileState.value = ProfileState.Loading

    viewModelScope.launch {
      userRepository.getUserProfile()
        .catch { throwable ->
          Log.e("ProfileViewModel", "Load profile error", throwable)
          _profileState.value = ProfileState.Error(throwable.message ?: "Unknown error")
        }
        .collect { user ->
          _profileState.value = ProfileState.Success(user)
        }
    }
  }

  fun logout() {
    viewModelScope.launch {
      userRepository.logout(getApplication())
    }
  }

  sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
  }
}
