package com.example.redminecapstoneproject.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ActivityDonorDetailBinding

class DonorDetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDonorDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDonorDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBack.setOnClickListener {
            finish()
        }
    }

}