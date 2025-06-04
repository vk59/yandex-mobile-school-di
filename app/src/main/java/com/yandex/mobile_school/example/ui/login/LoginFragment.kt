package com.yandex.mobile_school.example.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yandex.mobile_school.example.R
import com.yandex.mobile_school.example.appComponent
import com.yandex.mobile_school.example.databinding.FragmentLoginBinding
import com.yandex.mobile_school.example.ui.util.viewBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginFragment : Fragment() {

  private val binding by viewBinding(FragmentLoginBinding::bind)

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private lateinit var viewModel: LoginViewModel

  override fun onAttach(context: Context) {
    super.onAttach(context)

    requireContext().appComponent.inject(this)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_login, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class]
    setupListeners()
    observeViewModel()
  }

  private fun setupListeners() {
    binding.usernameEditText.doAfterTextChanged { text ->
      viewModel.setUsername(text.toString())
    }

    binding.passwordEditText.doAfterTextChanged { text ->
      viewModel.setPassword(text.toString())
    }

    binding.loginButton.setOnClickListener {
      viewModel.login()
    }
  }

  private fun observeViewModel() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.loginState.collect { state ->
        when (state) {
          is LoginViewModel.LoginState.Initial -> {
            // Initial state, do nothing
          }

          is LoginViewModel.LoginState.Loading -> {
            showLoading(true)
            showError(false)
          }

          is LoginViewModel.LoginState.Success -> {
            showLoading(false)
            navigateToProfile()
          }

          is LoginViewModel.LoginState.Error -> {
            showLoading(false)
            showError(true, state.message)
          }
        }
      }
    }
  }

  private fun showLoading(isLoading: Boolean) {
    binding.progressBar.isVisible = isLoading
    binding.loginButton.isEnabled = !isLoading
    binding.usernameEditText.isEnabled = !isLoading
    binding.passwordEditText.isEnabled = !isLoading
  }

  private fun showError(show: Boolean, message: String = "") {
    binding.errorTextView.isVisible = show
    binding.errorTextView.text = message
  }

  private fun navigateToProfile() {
    findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
  }
}
