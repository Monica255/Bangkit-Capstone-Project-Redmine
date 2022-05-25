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

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private var startSplash: ViewPropertyAnimator? = null
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //mAuth = FirebaseAuth.getInstance()
        val loginSignupViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[LoginSignupViewModel::class.java]

        startSplash = binding.imgLogo.animate().setDuration(splashDelay).alpha(1f).withEndAction {
            val intent = Intent(this, LoginActivity::class.java)
            val intent2 = Intent(this, DonorDataActivity::class.java)
            val intent3 = Intent(this, HomeActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

            loginSignupViewModel.firebaseUser.observe(this) { fu ->
                Log.d("TAG", "it.toString()")
                if (fu == null) {
                    startActivity(intent)
                    finish()
                } else {

                    loginSignupViewModel.getUserAccountData().observe(this) { value ->
                        //Log.d("TAG","sp "+it)
                        if (value == null) {

                            Log.d("TAG", "acc null")
                            loginSignupViewModel.setUserAccountData()

                        } else {
                            loginSignupViewModel.getUserDonorData().observe(this) {
                                //Log.d("TAG","sp2 "+it.toString())

                                if (it == null) {
                                    intent2.putExtra("name",value.name)
                                    startActivity(intent2)
                                    finish()
                                } else {
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