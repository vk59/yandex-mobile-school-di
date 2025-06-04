package com.yandex.mobile_school.example

import android.app.Application
import android.content.Context
import com.yandex.mobile_school.example.di.AppComponent
import com.yandex.mobile_school.example.di.DaggerAppComponent

class YandexMobileSchoolApplication : Application() {

  lateinit var appComponent: AppComponent

  override fun onCreate() {
    super.onCreate()

    appComponent = DaggerAppComponent.factory().create(this)
  }
}

val Context.appComponent: AppComponent
  get() = when (this) {
    is YandexMobileSchoolApplication -> appComponent
    else -> applicationContext.appComponent
  }
