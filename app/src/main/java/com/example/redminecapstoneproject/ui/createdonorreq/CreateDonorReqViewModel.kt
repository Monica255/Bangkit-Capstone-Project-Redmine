package com.example.redminecapstoneproject.ui.createdonorreq

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.repository.Repository
import com.example.redminecapstoneproject.ui.profile.UserDetailViewModel
import com.example.redminecapstoneproject.ui.testing.DonorRequest

//import com.example.redminecapstoneproject.ui.testing.DonorReq

//import com.example.redminecapstoneproject.ui.testing.DonorReq

class CreateDonorReqViewModel(private val provideRepository: Repository) :ViewModel() {
    var donorReq=DonorRequest()
    var provinceId:Int=0

    fun postDonorReq(data:DonorRequest,uid:String)=provideRepository.postDonorReq(data,uid)
    //fun getUserDonorData()=provideRepository.getUserDonorDataDb()
    var message=provideRepository.message

    fun getDonorReq()=provideRepository.getDonorReqDes()



}