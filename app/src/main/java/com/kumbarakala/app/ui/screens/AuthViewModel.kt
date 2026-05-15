package com.kumbarakala.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kumbarakala.app.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    object PasswordResetSent : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun resetState() { _authState.value = AuthState.Idle }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Please fill in all fields.")
            return
        }
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                repository.login(email, password).fold(
                    onSuccess = { _authState.value = AuthState.Success },
                    onFailure = { _authState.value = AuthState.Error("Invalid email or password") }
                )
            } catch (e: Exception) {
                // 🔥 SAFETY NET: Catches hard crashes
                _authState.value = AuthState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    fun signUp(email: String, password: String) {
        if (email.isBlank() || password.length < 6) {
            _authState.value = AuthState.Error("Password must be at least 6 characters.")
            return
        }
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                repository.signUp(email, password).fold(
                    onSuccess = { _authState.value = AuthState.Success },
                    onFailure = { _authState.value = AuthState.Error(it.message ?: "Signup failed") }
                )
            } catch (e: Exception) {
                // 🔥 SAFETY NET: Prevents the app from force-closing!
                _authState.value = AuthState.Error(e.message ?: "Connection failed. Try again.")
            }
        }
    }

    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _authState.value = AuthState.Error("Please enter your email.")
            return
        }
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                repository.resetPassword(email).fold(
                    onSuccess = { _authState.value = AuthState.PasswordResetSent },
                    onFailure = { _authState.value = AuthState.Error("Failed to send reset link.") }
                )
            } catch (e: Exception) {
                // 🔥 SAFETY NET: Catches hard crashes
                _authState.value = AuthState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }
}