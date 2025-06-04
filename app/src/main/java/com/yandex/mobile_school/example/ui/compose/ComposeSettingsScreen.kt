package com.yandex.mobile_school.example.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * A Jetpack Compose version of the Settings screen.
 * This demonstrates how to use Compose with the ViewModel and StateFlow.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeSettingsScreen(
  onNavigateBack: () -> Unit,
  viewModelFactory: ViewModelProvider.Factory
) {
  val viewModel: ComposeSettingsViewModel = viewModel(factory = viewModelFactory)

  val darkModeEnabled by viewModel.darkModeEnabled.collectAsState()
  val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
  val language by viewModel.language.collectAsState()
  val languages by viewModel.languages.collectAsState()
  val languageCodes by viewModel.languageCodes.collectAsState()

  var showLanguageDropdown by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Settings") },
      )
    }
  ) { paddingValues ->
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
      color = MaterialTheme.colorScheme.background
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
      ) {
        // Dark Mode Setting
        SettingItem(
          title = "Dark Mode",
          description = "Enable dark theme for the app",
          checked = darkModeEnabled,
          onCheckedChange = { isChecked ->
            viewModel.setDarkMode(isChecked)
          }
        )

        Spacer(
          modifier = Modifier
            .height(2.dp)
            .background(Color.Gray)
        )

        SettingItem(
          title = "Notifications",
          description = "Enable push notifications",
          checked = notificationsEnabled,
          onCheckedChange = { isChecked ->
            viewModel.setNotificationsEnabled(isChecked)
          }
        )

        Spacer(
          modifier = Modifier
            .height(2.dp)
            .background(Color.Gray)
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Language",
              style = MaterialTheme.typography.titleMedium
            )
            Text(
              text = "Select your preferred language",
              style = MaterialTheme.typography.bodyMedium
            )
          }

          Button(onClick = { showLanguageDropdown = true }) {
            Text(viewModel.getLanguageName(language))
          }

          DropdownMenu(
            expanded = showLanguageDropdown,
            onDismissRequest = { showLanguageDropdown = false }
          ) {
            languages.forEachIndexed { index, name ->
              DropdownMenuItem(
                text = { Text(name) },
                onClick = {
                  viewModel.setLanguage(languageCodes[index])
                  showLanguageDropdown = false
                }
              )
            }
          }
        }

        Spacer(
          modifier = Modifier
            .height(2.dp)
            .background(Color.Gray)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Reset Button
        Button(
          onClick = {
            viewModel.resetAllSettings()
          },
          modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
          Text("Reset All Settings")
        }
      }
    }
  }
}

@Composable
fun SettingItem(
  title: String,
  description: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium
      )
      Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium
      )
    }

    Spacer(modifier = Modifier.width(16.dp))

    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange
    )
  }
}
