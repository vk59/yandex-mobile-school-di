package com.yandex.mobile_school.example.di.module

import com.yandex.mobile_school.example.data.repository.MockUserRepository
import com.yandex.mobile_school.example.data.repository.SettingsRepository
import com.yandex.mobile_school.example.data.repository.SettingsRepositoryImpl
import com.yandex.mobile_school.example.data.repository.UserRepository
import com.yandex.mobile_school.example.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface RepositoryModule {

  @Binds
  @Singleton
  @MockImplementation
  fun bindsMockUserRepository(repository: MockUserRepository): UserRepository

  @Binds
  @Singleton
  @RealImplementation
  fun bindsRealUserRepository(repository: UserRepositoryImpl): UserRepository

  @Binds
  @Singleton
  fun bindsSettingsRepository(settings: SettingsRepositoryImpl): SettingsRepository
}
