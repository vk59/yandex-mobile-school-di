package com.yandex.mobile_school.example.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(
  private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    val viewModelProvider = viewModels[modelClass]
      ?: throw IllegalArgumentException("Unknown model class $modelClass")
    
    try {
      return viewModelProvider.get() as T
    } catch (e: Exception) {
      throw RuntimeException(e)
    }
  }
}
