package com.example.redminecapstoneproject.ui.testing

import androidx.lifecycle.MutableLiveData
import java.time.LocalDate

object test {
    fun generateDummyDonationEvents(): List<donationEvents> {
        val newsList = ArrayList<donationEvents>()
        for (i in 1..5) {
            val events = donationEvents(
                "Event name", "Event time"

            )
            newsList.add(events)
        }
        return newsList
    }

    fun generateDummyDonorRequest(): List<donorReq> {
        val newsList = ArrayList<donorReq>()
        for (i in 1..5) {
            val donorReq = donorReq(
                "Req name",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                "Denpasar"

            )
            newsList.add(donorReq)
        }
        return newsList
    }

    fun generateDummyProvince(): List<String> {
        val newsList = ArrayList<String>()
        for (i in 1..15) {
            val province = "Ini ceritanya province $i"
            newsList.add(province)
        }
        return newsList
    }

    fun generateDummyCity(): List<String> {
        val newsList = ArrayList<String>()
        for (i in 1..15) {
            val province = "Ini ceritanya city $i"
            newsList.add(province)
        }
        return newsList
    }

    fun generateLiveDummyProvince(): MutableLiveData<List<String>> {
        val list =MutableLiveData<List<String>>()
        val newsList = ArrayList<String>()
        newsList.add("Bali")
        for (i in 1..15) {
            val province = "Ini ceritanya province ${i+1}"
            newsList.add(province)
        }
        list.value=newsList
        return list
    }

    fun generateLiveDummyCity(): MutableLiveData<List<String>>{
        val list =MutableLiveData<List<String>>()
        val newsList = ArrayList<String>()
        newsList.add("Denpasar")
        for (i in 1..15) {
            val province = "Ini ceritanya city ${i+1}"
            newsList.add(province)
        }
        list.value=newsList
        return list
    }


    fun generateDummyUserDetail(): UserDetail {
        return UserDetail(
            true,
            "Dimas Ari Lumintang",
            "arilumintang@gmail.com",
            "87712345678",
            "Bali",
            "Denpasar"
        )
    }

    fun generateLiveDummyUserDetail(): MutableLiveData<UserDetail> {
        return MutableLiveData(
            UserDetail(
                true,
                "Dimas Ari Lumintang",
                "arilumintang@gmail.com",
                "87712345678",
                "Bali",
                "Denpasar"
            )
        )
    }

    fun generateDummyDonorDetail(): DonorDetail {
        return DonorDetail(
            false,
            "male",
            "a",
            "positive",
            true, false,
            LocalDate.of(2022, 5, 1),
            null
        )
    }

}

data class donationEvents(
    var name: String,
    var time: String
)

data class donorReq(
    var name: String,
    var des: String,
    var city: String
)

data class UserDetail(
    val isVerified: Boolean,
    var name: String,
    var email: String,
    var phoneNumber: String,
    var province: String,
    var city: String
)

data class DonorDetail(
    val isVerified: Boolean,
    val gender: String,
    var bloodType: String,
    var rhesus: String,
    var haveDonated: Boolean,
    var hadCovid: Boolean,
    var lastDonateDate: LocalDate? = null,
    var recoveryDate: LocalDate? = null,

    )