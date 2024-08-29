package com.amitranofinzi.vimata.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitranofinzi.vimata.data.dao.ChatDao
import com.amitranofinzi.vimata.data.dao.RelationshipDao
import com.amitranofinzi.vimata.data.dao.TestDao
import com.amitranofinzi.vimata.data.dao.TestSetDao
import com.amitranofinzi.vimata.data.dao.UserDao
import com.amitranofinzi.vimata.data.dao.WorkoutDao
import com.amitranofinzi.vimata.data.database.AppDatabase
import com.amitranofinzi.vimata.data.model.Test
import com.amitranofinzi.vimata.data.model.TestSet
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.data.model.Workout
import com.amitranofinzi.vimata.data.repository.AthleteRepository
import com.amitranofinzi.vimata.data.repository.TestRepository
import com.amitranofinzi.vimata.ui.navigation.InitializableViewModel
import kotlinx.coroutines.launch

/**
 * ViewModel for managing data related to athletes, workouts, and tests.
 * It interacts with repositories to fetch, update, and manage data, and provides
 * it to the UI through LiveData.
 */
class AthleteViewModel : ViewModel(), InitializableViewModel {

    // Application database and context
    lateinit var appDatabase: AppDatabase
    lateinit var context: Context
    private var isInitialized = false

    // Repositories for data access
    private lateinit var athleteRepository: AthleteRepository
    private lateinit var testRepository: TestRepository

    // DAOs for data access
    private val relationshipDao: RelationshipDao
        get() {
            return appDatabase.relationshipDao()
        }

    private val userDao: UserDao
        get() {
            return appDatabase.userDao()
        }

    private val workoutDao: WorkoutDao
        get() {
            return appDatabase.workoutDao()
        }

    private val chatDao: ChatDao
        get() {
            return appDatabase.chatDao()
        }

    /**
     * Initializes the ViewModel with the provided database and context.
     * This should be called before using the ViewModel.
     *
     * @param appDatabase The application database instance.
     * @param context The application context.
     */
    override fun initialize(appDatabase: AppDatabase, context: Context) {
        Log.d("AthleteViewModel", "initialize() called")
        if (!isInitialized) {
            this.appDatabase = appDatabase
            this.context = context
            Log.d("AthleteViewModel", "AppDatabase and Context initialized.")

            try {
                initializeRepositories()
                Log.d("AthleteViewModel", "Repositories initialized.")
            } catch (e: Exception) {
                Log.e("AthleteViewModel", "Error initializing repositories", e)
            }

            isInitialized = true
        } else {
            Log.d("AthleteViewModel", "Already initialized.")
        }
    }

    private fun initializeRepositories() {
        athleteRepository = AthleteRepository(relationshipDao, userDao, workoutDao, chatDao, context)
        testRepository = TestRepository(testDao, testSetDao, context)
    }


    private val _workouts = MutableLiveData<List<Workout>>()
    val workouts: LiveData<List<Workout>> = _workouts

    // trainers live dat
    private val _trainers = MutableLiveData<List<User>> ()
    val trainers: LiveData<List<User>> = _trainers


    private val testDao: TestDao by lazy { appDatabase.testDao() }
    private val testSetDao: TestSetDao by lazy { appDatabase.testSetDao() }


    private val _testSets = MutableLiveData<List<TestSet>>()
    val testSets: LiveData<List<TestSet>>  get() = _testSets


    private val _tests = MutableLiveData<List<Test>>()
    val tests: LiveData<List<Test>> get() = _tests

    /**
     * Fetches the list of workouts for a specific athlete.
     *
     * @param athleteID ID of the athlete.
     */
    fun fetchWorkouts(athleteID: String){
        viewModelScope.launch {
            _workouts.value = athleteRepository.getAthletesWorkouts(athleteID)
            Log.d("fetchWorkouts", workouts.toString())

        }
    }

    /**
     * Retrieves the list of trainers associated with a specific athlete.
     *
     * @param athleteId ID of the athlete.
     */
    fun getTrainersForAthletes(athleteId: String) {
        viewModelScope.launch {
            try {
                Log.d("AthleteViewModel", "Launching coroutine")
                val trainerIds = athleteRepository.getTrainerIdsForAthlete(athleteId)
                Log.d("AthleteViewModel", "trainers IDs fetched: $trainerIds")
                val trainerDetails = athleteRepository.getTrainers(trainerIds)
                Log.d("AthleteViewModel", "Trainer details fetched: $trainerDetails")
                _trainers.value = trainerDetails
                Log.d("AthleteViewModel", "Trainer details  assigned to LiveData")
                Log.d("AthleteViewModel", _trainers.toString())
            } catch (e: Exception) {
                // Handle the error
                Log.d("AthleteViewModel","error" )
                _trainers.value = emptyList()
            }
        }
    }

    /**
     * Fetches test sets associated with a specific athlete.
     *
     * @param athleteID ID of the athlete.
     */
    fun fetchTestSets(athleteID: String) {
        Log.d("testViewModel","fetchTestSets" )

        viewModelScope.launch {
            try {
                val fetchedTestSets = testRepository.getTestSetsForAthlete(athleteID)
                Log.d("testViewModel", "Fetched ${fetchedTestSets.size} test sets")
                fetchedTestSets.forEach {
                    Log.d("testViewModel", "TestSet: ${it.title}")
                }
                _testSets.setValue(fetchedTestSets)
                Log.d("testViewModel", "Test sets updated in LiveData")
            } catch (e: Exception) {
                // Handle the error
                Log.d("testViewModel","error fetching testSets" )
                _testSets.setValue(emptyList())
            }
        }
    }

    /**
     * Fetches tests associated with a specific test set.
     *
     * @param testSetId ID of the test set.
     */
    fun fetchTests(testSetId: String?) {
        viewModelScope.launch {
            try {
                val fetchedTests = testRepository.getTests(testSetId)
                Log.d("testViewModel", "Fetched ${fetchedTests.size} test sets")
                fetchedTests.forEach {
                    Log.d("testViewModel", "TestSet: ${it.exerciseName}")
                }
                _tests.setValue(fetchedTests)
            } catch (e: Exception) {
                // Handle the error
                Log.d("testViewModel","error fetching tests" )

            }
        }
    }

    /**
     * Updates the result of a specific test in the repository.
     *
     * @param test The test with updated result.
     */
    fun updateTestResult(test: Test) {
        viewModelScope.launch {
            try {
                Log.d("testViewModel","updating test result with test id: ${test.id}" )
                testRepository.updateTestResult(test)
            } catch (e: Exception) {
                // Handle the error
                Log.d("testViewModel","error updating test result" )
            }
        }
    }

    /**
     * Updates the status of a specific test in the repository.
     *
     * @param test The test with updated status.
     */
    fun updateTestStatus(test: Test) {
        viewModelScope.launch {
            try {
                Log.d("testViewModel","updating test status with test id: ${test.id}" )
                testRepository.updateTestStatus(test)
            } catch (e: Exception) {
                // Handle the error
                Log.d("testViewModel","error updating test status" )
            }
        }
    }

    /**
     * Starts a new chat and adds a trainer.
     *
     * @param email The email of the trainer to add.
     * @param currentUserId The ID of the current user.
     */
    fun addTrainerAndChat(email: String, currentUserId: String) {
        viewModelScope.launch {
            try {
                athleteRepository.startNewChat(email, currentUserId)
            } catch (e: Exception) {
                Log.e("addTrainer", "Failed")
            }
        }
    }




}