package com.yandex.mobile_school.example.di.module

import com.yandex.mobile_school.example.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestingModule {

  @Provides
  @Singleton
  fun provideUserRepository(
    @MockImplementation mockRepository: UserRepository
  ): UserRepository {
    return mockRepository
  }
}