package com.example.redminecapstoneproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.redminecapstoneproject.databinding.ActivityBloodDonationBinding
import com.example.redminecapstoneproject.ui.profile.DonorDetailActivity

class BloodDonationActivity : AppCompatActivity() {
    private lateinit var binding:ActivityBloodDonationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBloodDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cvDonorDetail.setOnClickListener {
            startActivity(Intent(this, DonorDetailActivity::class.java))
        }


        binding.btBloodDonationEvents.setOnClickListener {
            startActivity(Intent(this,DonationEventsActivity::class.java))
        }

        binding.btBloodDonorReq.setOnClickListener {
            startActivity(Intent(this,DonorRequestActivity::class.java))
        }

        binding.btBack.setOnClickListener{
            finish()
        }

    }
}