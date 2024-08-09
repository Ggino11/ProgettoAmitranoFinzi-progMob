package com.amitranofinzi.vimata.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitranofinzi.vimata.data.dao.ChatDao
import com.amitranofinzi.vimata.data.dao.RelationshipDao
import com.amitranofinzi.vimata.data.dao.UserDao
import com.amitranofinzi.vimata.data.dao.WorkoutDao
import com.amitranofinzi.vimata.data.database.AppDatabase
import com.amitranofinzi.vimata.data.model.FormField
import com.amitranofinzi.vimata.data.model.FormState
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.data.repository.AuthRepository
import com.amitranofinzi.vimata.ui.navigation.InitializableViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class AuthViewModel() : ViewModel(), InitializableViewModel {
    lateinit var appDatabase: AppDatabase
    lateinit var context: Context

    override fun initialize(appDatabase: AppDatabase, context: Context) {
        this.appDatabase = appDatabase
        this.context = context
    }
    private val relationshipDao: RelationshipDao by lazy { appDatabase.relationshipDao() }
    private val userDao: UserDao by lazy { appDatabase.userDao() }
    private val workoutDao: WorkoutDao by lazy { appDatabase.workoutDao() }
    private val chatDao: ChatDao by lazy { appDatabase.chatDao() }

    private val authRepository: AuthRepository by lazy {
        AuthRepository(
            relationshipDao = relationshipDao,
            userDao = userDao,
            workoutDao = workoutDao,
            chatDao = chatDao,
            context = context
        )
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _formState = MutableStateFlow(FormState())
    val formState: StateFlow<FormState> get() = _formState

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    fun getCurrentUserID() : String {
        val currentUser = authRepository.currentUser
        return currentUser?.uid ?: ""
    }

    fun fetchUser(userID : String) {
        viewModelScope.launch {
            val fetchedUser = authRepository.getUser(userID)
            Log.d("AuthViewModel", fetchedUser.toString())
            _user.value = fetchedUser
        }
    }
    //function to updates data stream for flow of formState
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
            Log.d("email_check","email use")
            // used to launch a coroutine. Inside this coroutine, the repository function authRepository.checkEmail(email) is called asynchronously
            val emailExists = authRepository.checkEmailExists(email)
            _formState.value = _formState.value.copy(
                emailErrorMessage = if (emailExists) "Email already exists" else "",
                emailError = emailExists)
            Log.d("email_check","email used fine routine")

        }
    }

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

    fun setAuthState(state: AuthState) {
        _authState.value = state
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
