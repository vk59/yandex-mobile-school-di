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
import kotlinx.coroutines.launch

class ProfileDetailsViewModel(application: Application) : AndroidViewModel(application) {
  private val userRepository: UserRepository = YandexMobileSchoolApplication.getUserRepository()

  private val _profileDetailsState = MutableStateFlow<ProfileDetailsState>(ProfileDetailsState.Loading)
  val profileDetailsState: StateFlow<ProfileDetailsState> = _profileDetailsState

  init {
    val userId = userRepository.getUserId(getApplication()) ?: "defaultUserId"
    loadUserDetails(userId)
  }

  private fun loadUserDetails(userId: String) {
    _profileDetailsState.value = ProfileDetailsState.Loading

    viewModelScope.launch {
      userRepository.getUserDetails(userId)
        .catch { throwable ->
          Log.e("ProfileDetailsViewModel", "Load user details error", throwable)
          _profileDetailsState.value = ProfileDetailsState.Error(throwable.message ?: "Unknown error")
        }
        .collect { user ->
          _profileDetailsState.value = ProfileDetailsState.Success(user)
        }
    }
  }

  sealed class ProfileDetailsState {

    data object Loading : ProfileDetailsState()

    data class Success(val user: User) : ProfileDetailsState()

    data class Error(val message: String) : ProfileDetailsState()
  }
}
