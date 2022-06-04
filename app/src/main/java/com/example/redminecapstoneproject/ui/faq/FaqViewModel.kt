package com.example.redminecapstoneproject.ui.faq

import androidx.lifecycle.ViewModel
import com.example.redminecapstoneproject.repository.Repository
import com.example.redminecapstoneproject.ui.testing.Faq

class FaqViewModel(private val provideRepository: Repository) : ViewModel() {

    val gelAllFaqDb=provideRepository.getAllFaqDb()

    fun getAllFaq(child: String)=provideRepository.getAllFaq(child)

    val isLoading=provideRepository.isLoading


}