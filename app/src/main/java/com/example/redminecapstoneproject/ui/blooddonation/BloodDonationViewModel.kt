package com.example.redminecapstoneproject.ui.blooddonation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.redminecapstoneproject.repository.Repository

class BloodDonationViewModel(private val provideRepository: Repository) :ViewModel() {
    fun getDonorReq()=provideRepository.getDonorReq()

    val donorReq=provideRepository.donorReq

    fun getAllVerifiedUsers()=provideRepository.getAllVerifiedUsers()

    fun getAllVerifiedDonorDataUsers()=provideRepository.getAllVerifiedDonorDataUsers()

    val validAccUsers=provideRepository.validAccUsers

    val validDonorDataUsers=provideRepository.validDonorDataUsers

    val isLoading= provideRepository.isLoading

    lateinit var filterProvince:String
    var filterCity=MutableLiveData<String>()

}