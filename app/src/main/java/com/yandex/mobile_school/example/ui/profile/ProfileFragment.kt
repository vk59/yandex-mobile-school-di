package com.yandex.mobile_school.example.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yandex.mobile_school.example.R
import com.yandex.mobile_school.example.data.model.User
import com.yandex.mobile_school.example.databinding.FragmentProfileBinding
import com.yandex.mobile_school.example.ui.compose.ComposeSettingsActivity
import com.yandex.mobile_school.example.ui.util.viewBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

  private val binding by viewBinding(FragmentProfileBinding::bind)
  private lateinit var viewModel: ProfileViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_profile, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

    setupListeners()
    observeViewModel()
  }

  private fun setupListeners() {
    binding.viewDetailsButton.setOnClickListener {
      findNavController().navigate(R.id.action_profileFragment_to_profileDetailsFragment)
    }

    binding.settingsButton.setOnClickListener {
      val intent = Intent(requireContext(), ComposeSettingsActivity::class.java)
      startActivity(intent)
    }

    binding.logoutButton.setOnClickListener {
      viewModel.logout()
      findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
    }
  }

  private fun observeViewModel() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.profileState.collect { state ->
        when (state) {
          is ProfileViewModel.ProfileState.Loading -> {
            showLoading(true)
            showError(false)
          }

          is ProfileViewModel.ProfileState.Success -> {
            showLoading(false)
            displayUserProfile(state.user)
          }

          is ProfileViewModel.ProfileState.Error -> {
            showLoading(false)
            showError(true, state.message)
          }
        }
      }
    }
  }

  private fun displayUserProfile(user: User) {
    binding.nameTextView.text = "${user.firstName} ${user.lastName}"
    binding.usernameTextView.text = "@${user.username}"
  }

  private fun showLoading(isLoading: Boolean) {
    binding.progressBar.isVisible = isLoading
    binding.avatarImageView.isVisible = !isLoading
    binding.nameTextView.isVisible = !isLoading
    binding.usernameTextView.isVisible = !isLoading
    binding.viewDetailsButton.isVisible = !isLoading
    binding.settingsButton.isVisible = !isLoading
    binding.logoutButton.isVisible = !isLoading
  }

  private fun showError(show: Boolean, message: String = "") {
    binding.errorTextView.isVisible = show
    binding.errorTextView.text = message
  }
}
