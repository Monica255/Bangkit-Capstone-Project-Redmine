package com.example.redminecapstoneproject.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.redminecapstoneproject.ui.testing.UserData
import com.example.redminecapstoneproject.ui.testing.AccountData
import com.example.redminecapstoneproject.ui.testing.test
import java.lang.Exception
import java.time.LocalDate

class UserDetailViewModel : ViewModel() {
    private var _provinces = test.generateLiveDummyProvince()
    var provinces: LiveData<List<String>> = _provinces

    private var _cities = MutableLiveData<List<String>>()
    var cities: LiveData<List<String>> = _cities

    var searchProvinceQuery: String? = null
    var searchCityQuery: String? = null

    private var _userData = test.generateLiveDummyUserData()
    var userData: LiveData<UserData> = _userData
    //lateinit var InitialuserData: UserData

    var _newUserData = test.generateLiveDummyUserData()

    private var _accountData = test.generateLiveDummyAccountData()
    var accountData: LiveData<AccountData> = _accountData

    var _newAccountData = test.generateLiveDummyAccountData()


    var temptLastDonateDate= test.generateLiveDummyUserData().value?.lastDonateDate
    var temptRecoveryDate = test.generateLiveDummyUserData().value?.recoveryDate


    fun isProvinceDif(): Boolean {
        return userData.value?.province != _newUserData.value?.province
    }

    fun getCities(province: String) {
        _cities = test.generateLiveDummyCity(province)
        cities = _cities
    }

    fun isDataDifferent(): Boolean {
        Log.d("TAG", "ad " + _accountData.value.toString())
        Log.d("TAG", "nad" + _newAccountData.value.toString())

        Log.d("TAG", "ud " + _userData.value.toString())
        Log.d("TAG", "nud" + _newUserData.value.toString())
        val x = _newUserData.value != _userData.value
        val y = _newAccountData.value != _accountData.value
        return x || y
    }

    fun updateUserDetail(vararg x:Pair<Any?, String>) {
        Log.d("TAG","start update")
        var tempAccountData: AccountData
        var tempUserData: UserData
        try {
            tempAccountData = _newAccountData.value!!
            tempUserData = _newUserData.value!!
            for(item in x){
                when (item.second) {
                    "name" -> {
                        tempAccountData.name = item.first as String
                    }
                    "email" -> {
                        tempAccountData.email = item.first as String

                    }
                    "gender" -> {
                        tempUserData.gender = item.first as String

                    }
                    "bloodType" -> {
                        tempUserData.bloodType = item.first as String

                    }
                    "rhesus" -> {
                        tempUserData.rhesus = item.first as String

                    }
                    "number" -> {
                        tempUserData.phoneNumber = item.first as String

                    }
                    "province" -> {
                        tempUserData.province = item.first as String
                        if (userData.value?.province != item.first) tempUserData.city = null else tempUserData.city=userData.value?.city
                    }
                    "city" -> {
                        tempUserData.city = item.first as String

                    }
                    "haveDonated" -> {
                        if(!(item.first as Boolean))tempUserData.lastDonateDate=null
                        tempUserData.haveDonated = item.first as Boolean

                    }
                    "hadCovid" -> {
                        if(!(item.first as Boolean))tempUserData.recoveryDate=null
                        tempUserData.hadCovid = item.first as Boolean

                    }
                    "lastDonateDate" -> {
                        tempUserData.lastDonateDate = item.first as LocalDate
                        temptLastDonateDate = item.first as LocalDate

                    }
                    "recoveryDate" -> {
                        tempUserData.recoveryDate = item.first as LocalDate
                        temptRecoveryDate = item.first as LocalDate

                    }
                }

            }
            _newUserData.value = tempUserData
            _newAccountData.value = tempAccountData
            Log.d("TAG","updated ud "+_newUserData.value )


        } catch (e: Exception) {

        }




    }

}