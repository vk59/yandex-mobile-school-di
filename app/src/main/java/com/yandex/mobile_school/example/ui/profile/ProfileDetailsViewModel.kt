package com.yandex.mobile_school.example.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.mobile_school.example.data.model.User
import com.yandex.mobile_school.example.domain.interactor.ProfileDetailsInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileDetailsViewModel @Inject constructor(
  private val profileDetailsInteractor: ProfileDetailsInteractor,
) : ViewModel() {

  private val _profileDetailsState = MutableStateFlow<ProfileDetailsState>(ProfileDetailsState.Loading)
  val profileDetailsState: StateFlow<ProfileDetailsState> = _profileDetailsState

  init {
    loadUserDetails()
  }

  private fun loadUserDetails() {
    _profileDetailsState.value = ProfileDetailsState.Loading

    viewModelScope.launch {
      profileDetailsInteractor.listenToUserDetails()
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
