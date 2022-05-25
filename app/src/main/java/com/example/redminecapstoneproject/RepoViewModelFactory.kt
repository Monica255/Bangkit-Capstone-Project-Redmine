package com.example.redminecapstoneproject

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.di.Injection
import com.example.redminecapstoneproject.ui.donordata.DonorDataViewModel
import com.example.redminecapstoneproject.ui.loginsignup.LoginSignupViewModel
import com.example.redminecapstoneproject.ui.profile.UserDetailViewModel

class RepoViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    companion object {
        @Volatile
        private var INSTANCE: RepoViewModelFactory? = null

        @JvmStatic
        fun getInstance(activity: Activity): RepoViewModelFactory {
            if (INSTANCE == null) {
                synchronized(RepoViewModelFactory::class.java) {
                    INSTANCE = RepoViewModelFactory(activity)
                }
            }
            return INSTANCE as RepoViewModelFactory
        }
    }


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(LoginSignupViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return LoginSignupViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(DonorDataViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return DonorDataViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(UserDetailViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return UserDetailViewModel(Injection.provideRepository(context)) as T
            }


        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}