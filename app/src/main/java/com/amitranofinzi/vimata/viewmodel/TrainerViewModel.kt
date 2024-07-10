package com.amitranofinzi.vimata.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.data.repository.TrainerRepository
import com.amitranofinzi.vimata.data.repository.WorkbookRepository
import kotlinx.coroutines.launch
import com.amitranofinzi.vimata.data.model.Exercise
import com.amitranofinzi.vimata.data.model.Collection


class TrainerViewModel: ViewModel() {

    private val trainerRepository : TrainerRepository = TrainerRepository()
    private val workbookRepository : WorkbookRepository = WorkbookRepository()

    private val _athletes = MutableLiveData<List<User>>()
    val athletes: LiveData<List<User>> = _athletes

    // Collection Live Data
    private val _collections = MutableLiveData<List<Collection>>()
    val collections: LiveData<List<Collection>> = _collections

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises

    private val _collection = MutableLiveData<Collection>()
    val collection: LiveData<Collection> = _collection

    // Athletes functions
    fun getAthletesForCoach(coachId: String) {
        viewModelScope.launch {
            try {
                Log.d("TrainerViewModel", "Launching coroutine")
                val athleteIds = trainerRepository.getAthleteIdsForCoach(coachId)
                Log.d("TrainerViewModel", "Athlete IDs: $athleteIds")
                val athleteDetails = trainerRepository.getAthletes(athleteIds)
                Log.d("TrainerViewModel", "Athlete details fetched: $athleteDetails")
                _athletes.value = athleteDetails
                Log.d("TrainerViewModel", "Athlete details assigned to LiveData")

            } catch (e: Exception) {
                // Handle the error
                Log.d("trainerViewModel","error" )
                _athletes.value = emptyList()
            }
        }
    }

    //Collection functions
    fun fetchCollections(trainerID: String) {
        viewModelScope.launch {
            try {
                Log.d("TrainerViewModel", "Fetching collections for trainerID: $trainerID")
                val fetchedCollections = workbookRepository.getCollections(trainerID)
                Log.d("TrainerViewModel", "Fetched collections: $fetchedCollections")
                _collections.value = fetchedCollections
            } catch (e: Exception) {
                Log.e("TrainerViewModel", "Error fetching collections", e)
                _collections.value = emptyList()
            }
        }
    }

    fun fetchCollection(collectionID: String){
        viewModelScope.launch {
            val fetchedCollection = workbookRepository.getCollectionByID(collectionID)
            _collection.value = fetchedCollection
        }
    }
    fun fetchExercises(collectionID: String?) {
        viewModelScope.launch {
            try {
                Log.d("TrainerViewModel", "Fetching exercises for exerciseIdList: $collectionID")
                val fetchedExercises = workbookRepository.getExercises(collectionID)
                Log.d("TrainerViewModel", "Fetched exercises: $fetchedExercises")
                _exercises.value = fetchedExercises
            } catch (e: Exception) {
                Log.e("TrainerViewModel", "Error fetching exercises", e)
                _exercises.value = emptyList()
            }
        }
    }

    fun addCollection(collection: Collection, trainerID: String) {
        viewModelScope.launch {
            workbookRepository.addCollection(collection.copy(trainerID = trainerID))
            fetchCollections(trainerID) // Refresh the list
        }
    }

    fun addExerciseToCollection(collectionId: String, exercise: Exercise, trainerID: String) {
        viewModelScope.launch {
            workbookRepository.addExerciseToCollection(collectionId, exercise.copy())
            fetchCollections(trainerID) // Refresh the list
        }
    }
}


