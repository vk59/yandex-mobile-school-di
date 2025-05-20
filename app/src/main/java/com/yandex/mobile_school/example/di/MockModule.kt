package com.yandex.mobile_school.example.di

import android.content.Context
import com.yandex.mobile_school.example.data.repository.MockUserRepository
import com.yandex.mobile_school.example.data.repository.UserRepository

/**
 * This module provides mock implementations for dependencies.
 * It's useful for testing, development, and demonstrations.
 * 
 * For the dependency injection lecture, this module shows how to:
 * 1. Provide a mock implementation instead of a real one
 * 2. Use the same interface with different implementations
 * 3. Switch between implementations without changing client code
 */
object MockModule {
    
    /**
     * Provides a mock implementation of UserRepository.
     * This can be used instead of the real implementation that uses the API.
     */
    fun provideUserRepository(): UserRepository {
        return MockUserRepository
    }
    
    /**
     * Initialize the mock repository with data from SharedPreferences.
     * This should be called in the Application class.
     */
    fun initializeMockData(context: Context) {
        val repository = MockUserRepository
        repository.loadAllUsersFromPrefs(context)
    }
    
    /**
     * Save mock data to SharedPreferences.
     * This can be called when the app is closing or when mock data changes.
     */
    fun saveMockData(context: Context) {
        val repository = MockUserRepository
        repository.saveAllUsersToPrefs(context)
    }
    
    /**
     * Add a new user to the mock database.
     * This is useful for testing registration functionality.
     */
    fun addMockUser(
        context: Context,
        username: String,
        password: String,
        email: String,
        firstName: String,
        lastName: String
    ) {
        val repository = MockUserRepository
        repository.addUser(
            username = username,
            password = password,
            email = email,
            firstName = firstName,
            lastName = lastName,
            context = context
        )
    }
}
