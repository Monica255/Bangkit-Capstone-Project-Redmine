package com.example.redminecapstoneproject.ui.blooddonation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.redminecapstoneproject.repository.Repository

class BloodDonationViewModel(private val provideRepository: Repository) :ViewModel() {
    fun getDonorReq()=provideRepository.getDonorReq()

    fun getDonationEvent()=provideRepository.getDonationEvent()

    val donationEvent=provideRepository.donorEvent

    val donorReq=provideRepository.donorReq


    fun getAllVerifiedUsers()=provideRepository.getAllVerifiedUsers()

    fun getAllVerifiedDonorDataUsers()=provideRepository.getAllVerifiedDonorDataUsers()

    val validAccUsers=provideRepository.validAccUsers

    val validDonorDataUsers=provideRepository.validDonorDataUsers

    val isLoading= provideRepository.isLoading

    var filterCity=MutableLiveData<String>()

}