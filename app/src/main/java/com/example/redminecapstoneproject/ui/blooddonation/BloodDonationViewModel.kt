package com.example.redminecapstoneproject.ui.blooddonation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.redminecapstoneproject.repository.Repository

class BloodDonationViewModel(private val provideRepository: Repository) :ViewModel() {


    fun getAllVerifiedUsers()=provideRepository.getAllVerifiedUsers()

    fun getAllVerifiedDonorDataUsers()=provideRepository.getAllVerifiedDonorDataUsers()

    val validAccUsers=provideRepository.validAccUsers

    val validDonorDataUsers=provideRepository.validDonorDataUsers

    val isLoading= provideRepository.isLoading
}