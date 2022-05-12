package com.example.redminecapstoneproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ActivityDonorRequestBinding
import com.example.redminecapstoneproject.ui.profile.DonorDetailActivity

class DonorRequestActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDonorRequestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDonorRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cvDonorDetail.setOnClickListener {
            startActivity(Intent(this, DonorDetailActivity::class.java))
        }


        binding.btBack.setOnClickListener {
            finish()
        }
    }
}