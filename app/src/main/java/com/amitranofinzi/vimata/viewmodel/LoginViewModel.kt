package com.amitranofinzi.vimata.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun login(email: String, password: String) {
        // Inserisci qui la logica di autenticazione
        // Questo esempio imposta semplicemente lo stato su Success dopo un "login"
        _loginState.value = LoginState.Success
    }

    sealed class LoginState {
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }
}