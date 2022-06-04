package com.example.redminecapstoneproject.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.redminecapstoneproject.repository.Repository

class HomeViewModel(private val provideRepository: Repository) :ViewModel() {
    val getAllFunFactDb=provideRepository.getAllFunFactsDb()

    fun getAllFunFact(child: String)=provideRepository.getAllFunFact(child)


    var funFact=MutableLiveData<String>()
}