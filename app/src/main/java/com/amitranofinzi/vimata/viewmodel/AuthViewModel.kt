package com.amitranofinzi.vimata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitranofinzi.vimata.data.model.FormField
import com.amitranofinzi.vimata.data.model.FormState
import com.amitranofinzi.vimata.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class AuthViewModel() : ViewModel() {

    private val authRepository : AuthRepository = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _formState = MutableStateFlow(FormState())
    val formState: StateFlow<FormState> get() = _formState

    fun getCurrentUserID() : FirebaseUser? {
        return authRepository.currentUser
    }

    //function to updtates data stream for flow of formState
    // takes in input the name of the field and the value
    fun updateField(field: FormField, value: String) {
        _formState.value = when(field) {
            FormField.NAME -> _formState.value.copy(name = value)
            FormField.SURNAME -> _formState.value.copy(surname = value)
           // FormField.USERNAME -> _formState.value.copy(username = value)
            FormField.USER_TYPE -> _formState.value.copy(userType = value)
            FormField.EMAIL -> _formState.value.copy(email = value)
            FormField.PASSWORD -> _formState.value.copy(password = value)
            FormField.CONFIRM_PASSWORD -> _formState.value.copy(confirmPassword = value)
            FormField.EMAIL_ERROR_MESSAGE -> _formState.value.copy(emailErrorMessage = value)
            FormField.PASSWORD_ERROR_MESSAGE -> _formState.value.copy(passwordErrorMessage = value)
            else -> _formState.value
        }

    }

    //check email exists
    fun emailAlreadyUsed(email: String){
        viewModelScope.launch {
            // used to launch a coroutine. Inside this coroutine, the repository function authRepository.checkEmail(email) is called asynchronously
            val emailExists = authRepository.checkEmailExists(email)
            _formState.value = _formState.value.copy(
                emailErrorMessage = if (emailExists) "Email already exists" else "",
                emailError = emailExists)
        }
    }
    /*validate password
    *(?=.*[A-Z]): to ensure at least one uppercase letter exists.
    *(?=.*[!@#\$%^&*()-_=+{};:,<.>]): to ensure at least one special character exists
    *(.{6,}):  to ensure at least 6 chars --> firebase default
    *  */
//    fun validatePassword(password: String) {
//        //regular expression
//        val regex = Regex("^(?=.*[A-Z])(?=.*[!@#\$%^&*()-_=+{};:,<.>])(.{6,})")
//        val isValid = regex.containsMatchIn(password)
//        _formState.value = _formState.value.copy(
//            passwordErrorMessage = if (isValid) "Password must contain at least one uppercase letter and one special character " else "",
//            passwordError = isValid)
//    }




    fun register(formState: FormState) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.register(formState.email, formState.password, formState.userType, formState.name, formState.surname)
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

    fun signOut() {
        authRepository.signOut()
        _authState.value = AuthState.Idle
    }


    //validate signUpform
    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Authenticated(val userType: String) : AuthState()
        //object Registered : AuthState()
        data class Error(val message: String) : AuthState()
    }



}
