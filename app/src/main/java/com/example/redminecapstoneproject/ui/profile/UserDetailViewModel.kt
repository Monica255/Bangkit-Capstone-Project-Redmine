package com.example.redminecapstoneproject.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.redminecapstoneproject.ui.testing.UserDetail
import com.example.redminecapstoneproject.ui.testing.test
import java.lang.Exception

class UserDetailViewModel : ViewModel() {
    private var _provinces = test.generateLiveDummyProvince()
    var provinces: LiveData<List<String>> = _provinces

    private var _cities = test.generateLiveDummyCity()
    var cities: LiveData<List<String>> = _cities

    var searchProvinceQuery: String? = null
    var searchCityQuery: String? = null

    private var _userDetail = test.generateLiveDummyUserDetail()
    var userDetail: LiveData<UserDetail> = _userDetail

    var _newUserDetail = MutableLiveData<UserDetail>()

    private lateinit var tempUserDetail: UserDetail


    fun isDataDifferent(): Boolean {
        Log.d("TAG", "nud " + _newUserDetail.value)
        Log.d("TAG", "ud  " + _userDetail.value)
        val x = _newUserDetail.value != _userDetail.value
        Log.d("TAG", x.toString())
        return _newUserDetail.value != _userDetail.value
    }


    fun updateUserDetail(value: Any, tag: String) {
        Log.d("TAG", _userDetail.value.toString())
        try {
            tempUserDetail = _newUserDetail.value!!
            when (tag) {
                "name" -> {
                    tempUserDetail.name = value as String
                }
                "email" -> {
                    tempUserDetail.email = value as String

                }
                "number" -> {
                    tempUserDetail.phoneNumber = value as String

                }
                "province" -> {
                    tempUserDetail.province = value as String

                }
                "city" -> {
                    tempUserDetail.city = value as String

                }
            }
            _newUserDetail.value = tempUserDetail
        } catch (e: Exception) {


        }


    }

}