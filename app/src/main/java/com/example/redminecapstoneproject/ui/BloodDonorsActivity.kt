package com.example.redminecapstoneproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ActivityBloodDonorsBinding

class BloodDonorsActivity : AppCompatActivity() {
    private lateinit var bindind:ActivityBloodDonorsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindind= ActivityBloodDonorsBinding.inflate(layoutInflater)
        setContentView(bindind.root)

        bindind.btBack.setOnClickListener {
            finish()
        }

    }
}