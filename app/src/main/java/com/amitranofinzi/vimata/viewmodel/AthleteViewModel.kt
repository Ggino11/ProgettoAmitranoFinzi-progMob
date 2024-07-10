package com.amitranofinzi.vimata.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitranofinzi.vimata.data.model.Test
import com.amitranofinzi.vimata.data.model.TestSet
import com.amitranofinzi.vimata.data.model.Workout
import com.amitranofinzi.vimata.data.repository.AthleteRepository
import com.amitranofinzi.vimata.data.repository.TestRepository
import kotlinx.coroutines.launch

class AthleteViewModel: ViewModel() {

    private val _workouts = MutableLiveData<List<Workout>>()
    val workouts: LiveData<List<Workout>> = _workouts


    // TEST live data
    private val testRepository : TestRepository = TestRepository()

    private val _testSets = MutableLiveData<List<TestSet>>()
    val testSets: LiveData<List<TestSet>>  get() = _testSets


    private val _tests = MutableLiveData<List<Test>>()
    val tests: LiveData<List<Test>> get() = _tests

    private val athleteRepository : AthleteRepository = AthleteRepository()


    fun fetchWorkouts(athleteID: String){
        viewModelScope.launch {
            _workouts.value = athleteRepository.getAthletesWorkouts(athleteID)
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



}