plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.ksp)
}

android {
  namespace = "com.yandex.mobile_school.example"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.yandex.mobile_school.example"
    minSdk = 26
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
  buildFeatures {
    compose = true
    viewBinding = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.8"
  }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.constraintlayout)

  implementation(libs.androidx.navigation.fragment.ktx)
  implementation(libs.androidx.navigation.ui.ktx)

  implementation(libs.androidx.lifecycle.viewmodel.ktx)
  implementation(libs.androidx.lifecycle.livedata.ktx)
  implementation(libs.androidx.fragment.ktx)

  implementation(libs.retrofit)
  implementation(libs.converter.gson)
  implementation(libs.logging.interceptor)

  implementation(libs.gson)

  implementation(libs.androidx.ui)
  implementation(libs.androidx.material3)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.material3.android)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  debugImplementation(libs.androidx.ui.tooling)

  implementation(libs.dagger)
  ksp(libs.dagger.compiler)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
}
