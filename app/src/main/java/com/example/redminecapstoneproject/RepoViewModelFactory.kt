package com.example.redminecapstoneproject

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.di.Injection
import com.example.redminecapstoneproject.ui.blooddonation.BloodDonationViewModel
import com.example.redminecapstoneproject.ui.createdonorreq.CreateDonorReqViewModel
import com.example.redminecapstoneproject.ui.donordata.DonorDataViewModel
import com.example.redminecapstoneproject.ui.faq.FaqViewModel
import com.example.redminecapstoneproject.ui.loginsignup.LoginSignupViewModel
import com.example.redminecapstoneproject.ui.mydonorreq.MyDonorReqViewModel
import com.example.redminecapstoneproject.ui.otp.OtpViewModel
import com.example.redminecapstoneproject.ui.profile.UserDetailViewModel
import com.example.redminecapstoneproject.ui.verifyaccount.VerifyAccountViewModel

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
            modelClass.isAssignableFrom(OtpViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return OtpViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(BloodDonationViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return BloodDonationViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(CreateDonorReqViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return CreateDonorReqViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(MyDonorReqViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return MyDonorReqViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(FaqViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return FaqViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(VerifyAccountViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return VerifyAccountViewModel(Injection.provideRepository(context)) as T
            }


        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}