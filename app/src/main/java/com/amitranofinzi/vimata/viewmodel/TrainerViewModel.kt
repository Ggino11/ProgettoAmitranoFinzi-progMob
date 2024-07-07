package com.amitranofinzi.vimata.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.data.repository.TrainerRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


class TrainerViewModel: ViewModel() {

    private val trainerRepository : TrainerRepository = TrainerRepository()
    private val _athletes = MutableLiveData<List<User>>()
    val athletes: LiveData<List<User>> = _athletes


    fun getAthletesForCoach(coachId: String) {
        viewModelScope.launch {
            try {
                val athleteIds = trainerRepository.getAthleteIdsForCoach(coachId)
                val athleteDetails = trainerRepository.getAthletes(athleteIds)
                _athletes.value = athleteDetails
                Log.d("vv", athleteIds.toString() )

            } catch (e: Exception) {
                // Handle the error
                _athletes.value = emptyList()
            }
        }
    }

}
