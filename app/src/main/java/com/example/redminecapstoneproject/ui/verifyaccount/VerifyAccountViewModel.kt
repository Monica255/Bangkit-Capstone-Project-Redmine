package com.example.redminecapstoneproject.ui.verifyaccount

import androidx.lifecycle.ViewModel
import com.example.redminecapstoneproject.repository.Repository
import com.example.redminecapstoneproject.ui.testing.Verification

class VerifyAccountViewModel(private val provideRepository: Repository) : ViewModel() {
    fun requestVerification(data:Verification)=provideRepository.requestVerification(data)

    fun getAccountData()=provideRepository.getUserAccountDataDb()

    val isLoading=provideRepository.isLoading

    val message=provideRepository.message

    val responseVerification=provideRepository.responseVerification
}