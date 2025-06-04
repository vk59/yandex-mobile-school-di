package com.yandex.mobile_school.example.ui.splash

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yandex.mobile_school.example.R
import com.yandex.mobile_school.example.appComponent
import com.yandex.mobile_school.example.domain.interactor.AuthInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Splash screen fragment that checks authentication status
 * and navigates to appropriate screen
 */
class SplashFragment : Fragment() {

  @Inject
  lateinit var authInteractor: AuthInteractor

  override fun onAttach(context: Context) {
    super.onAttach(context)
    requireContext().appComponent.inject(this)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_splash, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    // Check authentication after a short delay for better UX
    viewLifecycleOwner.lifecycleScope.launch {
      delay(1000) // Show splash for 1 second
      
      checkAuthenticationAndNavigate()
    }
  }

  private fun checkAuthenticationAndNavigate() {
    if (authInteractor.isUserLoggedIn()) {
      // User is logged in, navigate to profile
      findNavController().navigate(R.id.action_splashFragment_to_profileFragment)
    } else {
      // User is not logged in, navigate to login
      findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
    }
  }
}
