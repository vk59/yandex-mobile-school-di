package com.yandex.mobile_school.example.di

import com.yandex.mobile_school.example.MainActivity
import com.yandex.mobile_school.example.YandexMobileSchoolApplication
import com.yandex.mobile_school.example.ui.compose.ComposeSettingsActivity
import com.yandex.mobile_school.example.ui.login.LoginFragment
import com.yandex.mobile_school.example.ui.profile.ProfileDetailsFragment
import com.yandex.mobile_school.example.ui.profile.ProfileFragment
import com.yandex.mobile_school.example.ui.splash.SplashFragment

interface InjectFragments {

  fun inject(mainActivity: MainActivity)
  fun inject(splashFragment: SplashFragment)
  fun inject(loginFragment: LoginFragment)
  fun inject(profileFragment: ProfileFragment)
  fun inject(profileDetailsFragment: ProfileDetailsFragment)
  fun inject(composeSettingsActivity: ComposeSettingsActivity)
}