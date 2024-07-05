package com.amitranofinzi.vimata.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitranofinzi.vimata.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class AuthViewModel() : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val authRepository : AuthRepository = AuthRepository(firebaseAuth, firestore)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun register(email: String, password: String, userType: String, name: String, surname: String) {
        Log.d("view model", firebaseAuth.toString())
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.register(email, password, userType, name, surname)
            if (result.isSuccess) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val userTypeResult = authRepository.getUserType(userId)
                    if (userTypeResult.isSuccess) {
                        _authState.value = AuthState.Authenticated(userTypeResult.getOrNull()!!)
                    } else {
                        _authState.value = AuthState.Error(userTypeResult.exceptionOrNull()?.message ?: "Unknown Error")
                    }
                }
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
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val userTypeResult = authRepository.getUserType(userId)
                    if (userTypeResult.isSuccess) {
                        _authState.value = AuthState.Authenticated(userTypeResult.getOrNull()!!)
                    } else {
                        _authState.value = AuthState.Error(userTypeResult.exceptionOrNull()?.message ?: "Unknown Error")
                    }
                }
            } else {
                _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Unknown Error")
            }
        }
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Authenticated(val userType: String) : AuthState()
        //object Registered : AuthState()
        data class Error(val message: String) : AuthState()
    }



}
