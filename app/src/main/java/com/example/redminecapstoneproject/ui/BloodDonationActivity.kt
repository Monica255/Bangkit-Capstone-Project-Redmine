package com.example.redminecapstoneproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ActivityBloodDonationBinding

class BloodDonationActivity : AppCompatActivity() {
    private lateinit var binding:ActivityBloodDonationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBloodDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBloodDonationEvents.setOnClickListener {
            startActivity(Intent(this,DonorEventsActivity::class.java))
        }

        binding.btBloodDonorReq.setOnClickListener {
            startActivity(Intent(this,DonorRequestActivity::class.java))
        }

    }
}