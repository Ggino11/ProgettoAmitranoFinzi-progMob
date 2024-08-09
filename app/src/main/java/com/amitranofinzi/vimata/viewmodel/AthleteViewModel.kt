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
import com.amitranofinzi.vimata.data.model.Test
import com.amitranofinzi.vimata.data.model.TestSet
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.data.model.Workout
import com.amitranofinzi.vimata.data.repository.AthleteRepository
import com.amitranofinzi.vimata.data.repository.TestRepository
import com.amitranofinzi.vimata.ui.navigation.InitializableViewModel
import kotlinx.coroutines.launch

class AthleteViewModel : ViewModel(), InitializableViewModel {
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

    private val athleteRepository: AthleteRepository by lazy {
        AthleteRepository(
            relationshipDao = relationshipDao,
            userDao = userDao,
            workoutDao = workoutDao,
            chatDao = chatDao,
            context = context
        )
    }
    private val _workouts = MutableLiveData<List<Workout>>()
    val workouts: LiveData<List<Workout>> = _workouts

    // trainers live dat
    private val _trainers = MutableLiveData<List<User>> ()
    val trainers: LiveData<List<User>> = _trainers


    // TEST live data
    private val testRepository : TestRepository = TestRepository()

    private val _testSets = MutableLiveData<List<TestSet>>()
    val testSets: LiveData<List<TestSet>>  get() = _testSets


    private val _tests = MutableLiveData<List<Test>>()
    val tests: LiveData<List<Test>> get() = _tests

    fun fetchWorkouts(athleteID: String){
        viewModelScope.launch {
            _workouts.value = athleteRepository.getAthletesWorkouts(athleteID)
        }
    }

    //gets coaches for athlete
    fun getTrainersForAthletes(atheleteId: String) {
        viewModelScope.launch {
            try {
                Log.d("AthleteViewModel", "Launching coroutine")
                val trainerIds = athleteRepository.getTrainerIdsForAthlete(atheleteId)
                Log.d("TrainerViewModel", "trainers IDs fetched: $trainerIds")
                val trainerDetails = athleteRepository.getTrainers(trainerIds)
                Log.d("AthleteViewModel", "Athlete details fetched: $trainerDetails")
                _trainers.value = trainerDetails
                Log.d("AthleteViewModel", "Athlete details assigned to LiveData")
                Log.d("AthleteViewModel", _trainers.toString())
            } catch (e: Exception) {
                // Handle the error
                Log.d("AthleteViewModel","error" )
                _trainers.value = emptyList()
            }
        }
    }

    //TEST functions
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

    //add CHat and trainer
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