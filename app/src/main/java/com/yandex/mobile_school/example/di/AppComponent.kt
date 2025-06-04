package com.yandex.mobile_school.example.di

import android.content.Context
import com.yandex.mobile_school.example.MainActivity
import com.yandex.mobile_school.example.YandexMobileSchoolApplication
import com.yandex.mobile_school.example.di.module.AppModule
import com.yandex.mobile_school.example.di.module.NetworkModule
import com.yandex.mobile_school.example.di.module.RepositoryModule
import com.yandex.mobile_school.example.di.module.TestingModule
import com.yandex.mobile_school.example.di.module.ViewModelModule
import com.yandex.mobile_school.example.ui.compose.ComposeSettingsActivity
import com.yandex.mobile_school.example.ui.login.LoginFragment
import com.yandex.mobile_school.example.ui.profile.ProfileDetailsFragment
import com.yandex.mobile_school.example.ui.profile.ProfileFragment
import com.yandex.mobile_school.example.ui.splash.SplashFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    AppModule::class,
    TestingModule::class,
    NetworkModule::class,
    RepositoryModule::class,
    ViewModelModule::class,
  ]
)
interface AppComponent : InjectFragments {

  @Component.Factory
  interface Factory {

    fun create(@BindsInstance context: Context): AppComponent
  }
}
