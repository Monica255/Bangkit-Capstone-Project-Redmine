package com.example.redminecapstoneproject.ui.loginsignup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.redminecapstoneproject.repository.Repository
import com.google.firebase.auth.FirebaseUser

class LoginSignupViewModel(private val provideRepository: Repository) :ViewModel() {
    val message: LiveData<Pair<Boolean, String>> = provideRepository.message


    val isLoading: LiveData<Boolean> = provideRepository.isLoading

    val firebaseUser:LiveData<FirebaseUser?> =provideRepository.firebaseUser
    //val userAccountData:LiveData<RegisAccountData> =provideRepository.userAccountData
    //val userDonorData:LiveData<DonorData?> =provideRepository.userDonorData


    @JvmName("getUserAccountData1")
    fun getUserAccountData() =provideRepository.getUserAccountDataDb()

    @JvmName("getUserDonorData1")
    fun getUserDonorData()=provideRepository.getUserDonorDataDb()

    fun registerAccount(email:String,pass:String,name:String) {
        provideRepository.registerAccount(email,pass,name)
    }

    fun login(email: String,pass: String)=provideRepository.login(email,pass)

    fun signOut()=provideRepository.signOut()

    fun firebaseAuthWithGoogle(idToken:String)=provideRepository.firebaseAuthWithGoogle(idToken)

    fun setUserAccountData(name:String="Redminer")=provideRepository.setUserAccountData(name)
}