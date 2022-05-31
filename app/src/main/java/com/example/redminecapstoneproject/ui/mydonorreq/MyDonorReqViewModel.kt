package com.example.redminecapstoneproject.ui.mydonorreq

import androidx.lifecycle.ViewModel
import com.example.redminecapstoneproject.repository.Repository

class MyDonorReqViewModel(private val provideRepository: Repository) :ViewModel() {

    val donorReq=provideRepository.donorReq

    val isLoading=provideRepository.isLoading

    fun getMyDonorReq()=provideRepository.getMyDonoReq()

    fun getDonorData()=provideRepository.getUserDonorDataDb()
}