package com.yandex.mobile_school.example.ui.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.mobile_school.example.data.model.User
import com.yandex.mobile_school.example.domain.interactor.ProfileInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
  private val profileInteractor: ProfileInteractor
) : ViewModel() {

  private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
  val profileState: StateFlow<ProfileState> = _profileState

  init {
    loadUserProfile()
  }

  private fun loadUserProfile() {
    _profileState.value = ProfileState.Loading

    viewModelScope.launch {
      profileInteractor.getUserProfile()
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
      profileInteractor.logout()
    }
  }

  sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
  }
}
