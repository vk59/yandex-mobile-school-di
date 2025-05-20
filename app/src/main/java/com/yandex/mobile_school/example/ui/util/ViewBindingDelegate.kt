package com.yandex.mobile_school.example.ui.util

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * A delegate property for handling ViewBinding in Fragments.
 * This automatically handles the binding lifecycle and prevents memory leaks.
 *
 * Usage:
 * ```
 * private val binding by viewBinding(FragmentLoginBinding::bind)
 * ```
 */
class FragmentViewBindingDelegate<T : ViewBinding>(
  private val fragment: Fragment,
  private val bindingFactory: (View) -> T
) : ReadOnlyProperty<Fragment, T> {

  private var binding: T? = null

  init {
    fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
      override fun onCreate(owner: LifecycleOwner) {
        fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
          viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
              binding = null
            }
          })
        }
      }
    })
  }

  override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
    val binding = binding
    if (binding != null) {
      return binding
    }

    val lifecycle = fragment.viewLifecycleOwner.lifecycle
    if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
      throw IllegalStateException("Should not attempt to get bindings when Fragment views are destroyed.")
    }

    return bindingFactory(thisRef.requireView()).also { this.binding = it }
  }
}

/**
 * Extension function to create a ViewBinding delegate for a Fragment.
 *
 * Usage:
 * ```
 * private val binding by viewBinding(FragmentLoginBinding::bind)
 * ```
 */
fun <T : ViewBinding> Fragment.viewBinding(bindingFactory: (View) -> T): ReadOnlyProperty<Fragment, T> =
  FragmentViewBindingDelegate(this, bindingFactory)
