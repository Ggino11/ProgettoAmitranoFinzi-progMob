package com.amitranofinzi.vimata.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitranofinzi.vimata.data.model.Workout
import com.amitranofinzi.vimata.data.repository.AthleteRepository
import com.amitranofinzi.vimata.data.repository.AuthRepository
import com.amitranofinzi.vimata.data.repository.TrainerRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AthleteViewModel: ViewModel() {

    private val _workouts = MutableLiveData<List<Workout>>()
    val workouts: LiveData<List<Workout>> = _workouts

    private val athleteRepository : AthleteRepository = AthleteRepository()
    init {
        getWorkouts()
    }

    fun getWorkouts(){
        viewModelScope.launch {
            _workouts.value = athleteRepository.getAthletesWorkouts()
        }
    }


}