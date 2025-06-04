package com.yandex.mobile_school.example.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yandex.mobile_school.example.ui.compose.ComposeSettingsViewModel
import com.yandex.mobile_school.example.ui.login.LoginViewModel
import com.yandex.mobile_school.example.ui.profile.ProfileDetailsViewModel
import com.yandex.mobile_school.example.ui.profile.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

  @Binds
  abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

  @Binds
  @IntoMap
  @ViewModelKey(LoginViewModel::class)
  abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(ProfileViewModel::class)
  abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(ProfileDetailsViewModel::class)
  abstract fun bindProfileDetailsViewModel(profileDetailsViewModel: ProfileDetailsViewModel): ViewModel


  @Binds
  @IntoMap
  @ViewModelKey(ComposeSettingsViewModel::class)
  abstract fun bindComposeSettingsViewModel(composeSettingsViewModel: ComposeSettingsViewModel): ViewModel
}
