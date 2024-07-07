package com.amitranofinzi.vimata.viewmodel

import androidx.lifecycle.ViewModel
import com.amitranofinzi.vimata.data.repository.AthleteRepository
import com.amitranofinzi.vimata.data.repository.AuthRepository
import com.amitranofinzi.vimata.data.repository.TrainerRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AthleteViewModel: ViewModel() {

    private val authRepository : AthleteRepository = AthleteRepository()

}