package com.example.redminecapstoneproject.ui.donordata

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.redminecapstoneproject.repository.Repository
import com.example.redminecapstoneproject.ui.testing.DonorData
import java.time.LocalDate

class DonorDataViewModel(private val provideRepository:Repository):ViewModel() {
    var donorData=DonorData()

    var tempLastDonateDate:LocalDate?=null
    var tempRecoveryDate:LocalDate?=null

    var name=""

    val message: LiveData<Pair<Boolean, String>> = provideRepository.message

    val isLoading: LiveData<Boolean> = provideRepository.isLoading

    fun setUserDonorData(donorData: DonorData)=provideRepository.setUserDonorData(donorData)

}