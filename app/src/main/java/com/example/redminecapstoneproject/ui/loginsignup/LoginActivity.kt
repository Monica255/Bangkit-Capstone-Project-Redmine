package com.example.redminecapstoneproject.ui.loginsignup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.redminecapstoneproject.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager.beginTransaction().add(R.id.frame_container,LoginFragment(),"login_fragment")
        supportFragmentManager.beginTransaction().add(R.id.frame_container,SignupFragment(),"signup_fragment")

    }
}