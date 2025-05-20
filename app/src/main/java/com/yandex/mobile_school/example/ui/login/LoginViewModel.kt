package com.yandex.mobile_school.example.ui.login

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

class LoginViewModel(application: Application) : AndroidViewModel(application) {

  private val userRepository: UserRepository = YandexMobileSchoolApplication.getUserRepository()

  private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
  val loginState: StateFlow<LoginState> = _loginState

  private val _username = MutableStateFlow("")
  val username: StateFlow<String> = _username

  private val _password = MutableStateFlow("")
  val password: StateFlow<String> = _password

  fun setUsername(value: String) {
    _username.value = value
  }

  fun setPassword(value: String) {
    _password.value = value
  }

  fun validateInputs(): Boolean {
    val usernameValue = username.value
    val passwordValue = password.value

    if (usernameValue.isEmpty()) {
      _loginState.value = LoginState.Error("Username cannot be empty")
      return false
    }

    if (passwordValue.isEmpty()) {
      _loginState.value = LoginState.Error("Password cannot be empty")
      return false
    }

    if (passwordValue.length < 6) {
      _loginState.value = LoginState.Error("Password must be at least 6 characters")
      return false
    }

    return true
  }

  fun login() {
    if (!validateInputs()) {
      return
    }

    _loginState.value = LoginState.Loading

    viewModelScope.launch {
      try {
        userRepository.login(
          getApplication(),
          username.value,
          password.value
        ).catch { throwable ->
          Log.e("LoginViewModel", "Login error", throwable)
          _loginState.value = LoginState.Error(throwable.message ?: "Unknown error")
        }.collect { user ->
          _loginState.value = LoginState.Success(user)
        }
      } catch (e: Exception) {
        Log.e("LoginViewModel", "Login error", e)
        _loginState.value = LoginState.Error(e.message ?: "Unknown error")
      }
    }
  }

  sealed class LoginState {
    object Initial : LoginState()
    object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
  }
}
