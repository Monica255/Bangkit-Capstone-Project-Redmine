package com.example.redminecapstoneproject.ui.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.redminecapstoneproject.repository.Repository

class OtpViewModel(private val provideRepository: Repository) :ViewModel() {
    val isLoading: LiveData<Boolean> = provideRepository.isLoading

    val responseOtp=provideRepository.responseOtp

    fun getOtpCOde()=provideRepository.getOtpCode()

    fun sendOtpCOde(email:String)=provideRepository.sendOtp(email)

    val otpCode: LiveData<String?> = provideRepository.otpCode
}