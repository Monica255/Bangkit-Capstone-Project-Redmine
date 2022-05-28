package com.example.redminecapstoneproject.ui.mydonorreq

import androidx.lifecycle.ViewModel
import com.example.redminecapstoneproject.repository.Repository

class MyDonorReqViewModel(private val provideRepository: Repository) :ViewModel() {

    val donorReq=provideRepository.donorReq



    fun getMyDonorReq()=provideRepository.getMyDonoReq()

}