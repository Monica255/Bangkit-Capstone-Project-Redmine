package com.example.redminecapstoneproject.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewPropertyAnimator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.ActivitySplashBinding
import com.example.redminecapstoneproject.ui.donordata.DonorDataActivity
import com.example.redminecapstoneproject.ui.loginsignup.LoginActivity
import com.example.redminecapstoneproject.ui.loginsignup.LoginSignupViewModel
import com.example.redminecapstoneproject.ui.home.HomeActivity
import com.example.redminecapstoneproject.ui.otp.OtpActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private var startSplash: ViewPropertyAnimator? = null
    private lateinit var binding: ActivitySplashBinding
    private var startIntent=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loginSignupViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[LoginSignupViewModel::class.java]

        loginSignupViewModel.getUserDonorData()
        loginSignupViewModel.getUserAccountData()
        startSplash = binding.imgLogo.animate().setDuration(splashDelay).alpha(1f).withEndAction {
            val intent = Intent(this, LoginActivity::class.java)
            val intent2 = Intent(this, DonorDataActivity::class.java)
            val intent3 = Intent(this, HomeActivity::class.java)
            val intent4 = Intent(this, OtpActivity::class.java)



                loginSignupViewModel.firebaseUser.observe(this) { fu ->
                    if (fu == null && startIntent) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startIntent=false
                        startActivity(intent)
                        finish()
                    } else {

                        loginSignupViewModel.getUserAccountDataDb().observe(this) { value ->
                            if (value == null) {
                                loginSignupViewModel.setUserAccountData()
                            } else if (value.otpCode == null && startIntent) {
                                intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startIntent=false
                                startActivity(intent4)
                                finish()
                            } else {
                                loginSignupViewModel.getUserDonorDataDb().observe(this) {
                                    //Log.d("TAG","sp2 "+it.toString())
                                    //Log.d("TAG","sp "+it.toString())

                                    if (it == null && startIntent) {
                                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                        intent2.putExtra("name", value.name)
                                        startIntent=false
                                        startActivity(intent2)
                                        finish()
                                    } else if (startIntent){
                                        //intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startIntent=false
                                        startActivity(intent3)
                                        finish()
                                    }
                                }
                            }
                        }


                    }
                }



        }
    }

    override fun onDestroy() {
        startSplash?.cancel()
        super.onDestroy()
    }

    companion object {
        private var splashDelay: Long = 2_500L
    }
}