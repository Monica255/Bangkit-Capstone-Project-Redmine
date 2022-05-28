package com.example.redminecapstoneproject.ui.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.redminecapstoneproject.repository.Repository
import com.google.firebase.auth.FirebaseUser

class OtpViewModel(private val provideRepository: Repository) :ViewModel() {
    val isLoading: LiveData<Boolean> = provideRepository.isLoading

    val firebaseUser: LiveData<FirebaseUser?> =provideRepository.firebaseUser

    fun getOtpCOde()=provideRepository.getOtpCode()

    val otpCode: LiveData<String?> = provideRepository.otpCode
}