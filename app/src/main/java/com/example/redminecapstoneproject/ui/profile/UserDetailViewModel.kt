package com.example.redminecapstoneproject.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.redminecapstoneproject.repository.Repository
import com.example.redminecapstoneproject.ui.testing.DonorDataRoom
import com.example.redminecapstoneproject.ui.testing.RegisAccountDataRoom
import com.example.redminecapstoneproject.ui.testing.test

class UserDetailViewModel(private val provideRepository: Repository) : ViewModel() {
    var mProvince=provideRepository.listProvinces
    var mCity=provideRepository.listCities


    var searchProvinceQuery: String? = null
    var searchCityQuery: String? = null

    private var _userData = provideRepository.getUserDonorDataDb()
    var userData: LiveData<DonorDataRoom> = _userData
    //lateinit var InitialuserData: UserData

    var _newUserData = MutableLiveData<DonorDataRoom>()

    private var _accountData = provideRepository.getUserAccountDataDb()
    var accountData: LiveData<RegisAccountDataRoom> = _accountData

    var _newAccountData = MutableLiveData<RegisAccountDataRoom>()


    var temptLastDonateDate = provideRepository.getUserDonorDataDb().value?.lastDonateDate
    var temptRecoveryDate = provideRepository.getUserDonorDataDb().value?.recoveryDate

    val message= provideRepository.message
    val isLoading=provideRepository.isLoading

    init {
        _newUserData.value = _userData.value
        _newAccountData.value = _accountData.value
        provideRepository.getProvinces()
        //Log.d("TAG",)
    }

    fun saveUserDonorData(data:DonorDataRoom)=provideRepository.saveUserDonorData(data)
    fun saveUserAccountData(data:RegisAccountDataRoom)=provideRepository.saveUserAccountData(data)


    fun isProvinceDif(): Boolean {
        return userData.value?.province != _newUserData.value?.province
    }

    fun getCities(id: Int) {
        /*_cities = test.generateLiveDummyCity(province)
        cities = _cities*/
        provideRepository.getCities(id)
    }

    fun isDataDifferent(): Boolean {
        Log.d("TAG", "ad " + _accountData.value.toString())
        Log.d("TAG", "nad" + _newAccountData.value.toString())

        Log.d("DD", "ud " + _userData.value.toString())
        Log.d("DD", "nud" + _newUserData.value.toString())
        val x = _newUserData.value != _userData.value
        val y = _newAccountData.value != _accountData.value
        return x || y
    }

    fun updateAccountDataRoom(vararg x: Pair<Any?, String>) {
        Log.d("TAG", x[0].toString())
        try {
            val tempAccountData: RegisAccountDataRoom = _newAccountData.value!!
            for (item in x) {
                when (item.second) {
                    "name" -> {
                        tempAccountData.name = item.first as String
                    }
                    "email" -> {
                        tempAccountData.email = item.first as String

                    }
                }
            }
            _newAccountData.value = tempAccountData
        }catch (e:Exception){
            Log.e("ERROR",e.message.toString())
        }

    }

    fun updateDonorDataRoom(vararg x: Pair<Any?, String>) {
        Log.d("TAG", x[0].toString())
        try {
        val tempUserData: DonorDataRoom = _newUserData.value!!
        for (item in x) {
            when (item.second) {
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
                    if (userData.value?.province != item.first) tempUserData.city =
                        null else tempUserData.city = userData.value?.city
                }
                "city" -> {
                    tempUserData.city = item.first as String

                }
                "haveDonated" -> {
                    if (!(item.first as Boolean)) tempUserData.lastDonateDate = null
                    tempUserData.haveDonated = item.first as Boolean

                }
                "hadCovid" -> {
                    if (!(item.first as Boolean)) tempUserData.recoveryDate = null
                    tempUserData.hadCovid = item.first as Boolean

                }
                "lastDonateDate" -> {
                    tempUserData.lastDonateDate = item.first.toString()
                    temptLastDonateDate = item.first.toString()

                }
                "recoveryDate" -> {
                    tempUserData.recoveryDate = item.first.toString()
                    temptRecoveryDate = item.first.toString()

                }
            }

        }
        _newUserData.value = tempUserData
        //_newAccountData.value = tempAccountData
        Log.d("TAG", "updated ud " + _newUserData.value)


        } catch (e: Exception) {
            Log.d("TAG","updated ud "+"error")

        }


    }

}