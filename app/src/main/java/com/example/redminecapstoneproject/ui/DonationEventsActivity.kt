package com.example.redminecapstoneproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ActivityDonationEventsBinding
import com.example.redminecapstoneproject.ui.profile.DonorDetailActivity

class DonationEventsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDonationEventsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDonationEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cvDonorDetail.setOnClickListener {
            startActivity(Intent(this, DonorDetailActivity::class.java))
        }


        binding.btBack.setOnClickListener {
            finish()
        }
    }
}