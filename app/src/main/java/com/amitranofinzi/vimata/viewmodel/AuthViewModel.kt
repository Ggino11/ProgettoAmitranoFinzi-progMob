package com.amitranofinzi.vimata.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitranofinzi.vimata.data.dao.ChatDao
import com.amitranofinzi.vimata.data.dao.CollectionDao
import com.amitranofinzi.vimata.data.dao.ExerciseDao
import com.amitranofinzi.vimata.data.dao.MessageDao
import com.amitranofinzi.vimata.data.dao.RelationshipDao
import com.amitranofinzi.vimata.data.dao.TestDao
import com.amitranofinzi.vimata.data.dao.TestSetDao
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

/**
 * ViewModel for managing authentication processes such as registration, login, and user management.
 * It interacts with the AuthRepository and provides authentication state and user information through LiveData and StateFlow.
 */
class AuthViewModel : ViewModel(), InitializableViewModel {

    private lateinit var appDatabase: AppDatabase
    private lateinit var context: Context
    private var isInitialized = false

    // DAO dichiarati senza lazy
    private val relationshipDao: RelationshipDao
        get() = appDatabase.relationshipDao()

    private val testSetDao: TestSetDao
        get() = appDatabase.testSetDao()

    private val userDao: UserDao
        get() = appDatabase.userDao()

    private val workoutDao: WorkoutDao
        get() = appDatabase.workoutDao()

    private val chatDao: ChatDao
        get() = appDatabase.chatDao()

    private val exerciseDao: ExerciseDao
        get() = appDatabase.exerciseDao()

    private val collectionDao: CollectionDao
        get() = appDatabase.collectionDao()

    private val messageDao: MessageDao
        get() = appDatabase.messageDao()

    private val testDao: TestDao
        get() = appDatabase.testDao()

    // Repository dichiarati con lazy
    private lateinit var authRepository: AuthRepository

    /**
     * Initializes the ViewModel with the provided database and context.
     * This should be called before using the ViewModel.
     *
     * @param appDatabase The application database instance.
     * @param context The application context.
     */
    override fun initialize(appDatabase: AppDatabase, context: Context) {
        if (!isInitialized) {
            this.appDatabase = appDatabase
            this.context = context
            isInitialized = true


            authRepository = AuthRepository(
                relationshipDao = relationshipDao,
                testSetDao = testSetDao,
                userDao = userDao,
                workoutDao = workoutDao,
                chatDao = chatDao,
                context = context,
                exerciseDao = exerciseDao,
                collectionDao = collectionDao,
                testDao = testDao,
                messageDao = messageDao
            )
        }
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _formState = MutableStateFlow(FormState())
    val formState: StateFlow<FormState> get() = _formState

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    /**
     * Returns the ID of the current authenticated user.
     *
     * @return The user ID of the currently authenticated user.
     */
    fun getCurrentUserID() : String {
        val currentUser = authRepository.currentUser
        return currentUser?.uid ?: ""
    }

    /**
     * Fetches user information for a given user ID.
     *
     * @param userID The ID of the user to fetch.
     */
    fun fetchUser(userID : String) {
        viewModelScope.launch {
            val fetchedUser = authRepository.getUser(userID)
            Log.d("AuthViewModel", fetchedUser.toString())
            _user.value = fetchedUser
        }
    }

    /**
     * Updates a specific form field in the form state.
     *
     * @param field The form field to update.
     * @param value The new value for the form field.
     */
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

    /**
     * Checks if the provided email is already used.
     *
     * @param email The email to check.
     */
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

    /**
     * Registers a new user with the provided form state.
     *
     * @param formState The form state containing registration details.
     */
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

    /**
     * Logs in a user with the provided email and password.
     *
     * @param email The user's email.
     * @param password The user's password.
     */
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

    /**
     * Signs out the current user.
     */
    fun signOut() {
        authRepository.signOut()
        _authState.value = AuthState.Idle
    }

    /**
     * Sets the authentication state.
     *
     * @param state The new authentication state.
     */
    fun setAuthState(state: AuthState) {
        _authState.value = state
    }


    /**
     * Represents the different states of authentication.
     */
    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Authenticated(val userType: String) : AuthState()
        //object Registered : AuthState()
        data class Error(val message: String) : AuthState()
    }



}
