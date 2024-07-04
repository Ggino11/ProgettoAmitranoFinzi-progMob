package com.amitranofinzi.vimata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitranofinzi.vimata.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel(
) {
//maybe live data
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun register(email: String, password: String, userType: String, name: String, surname: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.register(email, password, userType, name, surname)
            if (result.isSuccess) {
                _authState.value = AuthState.Registered
            } else {
                _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Unknown Error")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.login(email, password)
            if (result.isSuccess) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Unknown Error")
            }
        }
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        object Authenticated : AuthState()
        object Registered : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
