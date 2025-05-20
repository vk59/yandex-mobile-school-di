package com.yandex.mobile_school.example.ui.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

/**
 * Activity that hosts the Compose Settings screen.
 * This demonstrates how to integrate Jetpack Compose with an existing XML-based app.
 */
class ComposeSettingsActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          ComposeSettingsScreen(
            onNavigateBack = { finish() }
          )
        }
      }
    }
  }
}
