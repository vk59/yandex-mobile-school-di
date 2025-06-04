package com.yandex.mobile_school.example.di.module

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MockImplementation

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RealImplementation

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationContext

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityContext
