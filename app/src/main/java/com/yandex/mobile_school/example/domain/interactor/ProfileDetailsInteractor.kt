package com.yandex.mobile_school.example.domain.interactor

import com.yandex.mobile_school.example.data.model.User
import com.yandex.mobile_school.example.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileDetailsInteractor @Inject constructor(
  private val userRepository: UserRepository,
) {

  fun listenToUserDetails(): Flow<User> {
    return userRepository.getUserProfile()
  }
}