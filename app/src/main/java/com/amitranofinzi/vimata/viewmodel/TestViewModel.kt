package com.amitranofinzi.vimata.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.amitranofinzi.vimata.data.model.Test
import com.amitranofinzi.vimata.data.model.TestSet
import com.amitranofinzi.vimata.data.repository.TestRepository
import kotlinx.coroutines.launch

class TestViewModel: ViewModel() {

    private val testRepository : TestRepository = TestRepository()

    private val _testSets = MutableLiveData<List<TestSet>>()
    val testSets: LiveData<List<TestSet>> = _testSets

    private val _tests = MutableLiveData<List<Test>>()
    val tests: LiveData<List<Test>> = _tests

    fun fetchTestSets(athleteID: String) {
        Log.d("testViewModel","fetchTestSets" )

        viewModelScope.launch {
            try {
                val fetchedTestSets = testRepository.getTestSetsForAthlete(athleteID)
                Log.d("testViewModel",fetchedTestSets[0].title )
                _testSets.value = fetchedTestSets

            } catch (e: Exception) {
                // Handle the error
                Log.d("testViewModel","error" )
                _testSets.value = emptyList()
            }
        }
    }

    fun fetchTests(testSetId: String?) {
        viewModelScope.launch {
            try {
                val fetchedTests = testRepository.getTests(testSetId)
                _tests.value = fetchedTests
            } catch (e: Exception) {
                // Handle the error
            }
        }
    }

}
