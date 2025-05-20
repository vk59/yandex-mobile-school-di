package com.yandex.mobile_school.example.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.yandex.mobile_school.example.R
import com.yandex.mobile_school.example.data.model.User
import com.yandex.mobile_school.example.databinding.FragmentProfileDetailsBinding
import com.yandex.mobile_school.example.ui.util.viewBinding
import kotlinx.coroutines.launch

class ProfileDetailsFragment : Fragment() {

  private val binding by viewBinding(FragmentProfileDetailsBinding::bind)
  private lateinit var viewModel: ProfileDetailsViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_profile_details, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel = ViewModelProvider(this)[ProfileDetailsViewModel::class.java]
    observeViewModel()
  }

  private fun observeViewModel() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.profileDetailsState.collect { state ->
        when (state) {
          is ProfileDetailsViewModel.ProfileDetailsState.Loading -> {
            showLoading(true)
            showError(false)
          }

          is ProfileDetailsViewModel.ProfileDetailsState.Success -> {
            showLoading(false)
            displayUserDetails(state.user)
          }

          is ProfileDetailsViewModel.ProfileDetailsState.Error -> {
            showLoading(false)
            showError(true, state.message)
          }
        }
      }
    }
  }

  private fun displayUserDetails(user: User) {
    binding.nameTextView.text = "${user.firstName} ${user.lastName}"
    binding.usernameTextView.text = "@${user.username}"
    binding.emailTextView.text = user.email
    binding.phoneTextView.text = user.phone
    binding.addressTextView.text = user.address
    binding.bioTextView.text = user.bio
  }

  private fun showLoading(isLoading: Boolean) {
    binding.progressBar.isVisible = isLoading
    binding.avatarImageView.isVisible = !isLoading
    binding.nameTextView.isVisible = !isLoading
    binding.usernameTextView.isVisible = !isLoading
    binding.emailTextView.isVisible = !isLoading
    binding.phoneTextView.isVisible = !isLoading
    binding.addressTextView.isVisible = !isLoading
    binding.bioTextView.isVisible = !isLoading
  }

  private fun showError(show: Boolean, message: String = "") {
    binding.errorTextView.isVisible = show
    binding.errorTextView.text = message
  }
}
