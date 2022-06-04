package com.example.redminecapstoneproject.ui.createdonorreq

import androidx.lifecycle.ViewModel
import com.example.redminecapstoneproject.repository.Repository
import com.example.redminecapstoneproject.ui.testing.DonorRequest

//import com.example.redminecapstoneproject.ui.testing.DonorReq

//import com.example.redminecapstoneproject.ui.testing.DonorReq

class CreateDonorReqViewModel(private val provideRepository: Repository) :ViewModel() {
    var donorReq=DonorRequest()


    fun postDonorReq(data:DonorRequest,uid:String)=provideRepository.postDonorReq(data,uid)
    //fun getUserDonorData()=provideRepository.getUserDonorDataDb()
    var message=provideRepository.message

    fun deleteDonorReq(id:String)=provideRepository.deleteDonorReq(id)

    var accountData = provideRepository.getUserAccountDataDb()
    var donorData = provideRepository.getUserDonorDataDb()

    val isLoading=provideRepository.isLoading




}