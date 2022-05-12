package com.example.redminecapstoneproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ActivityCreateDonorReqBinding

class CreateDonorReqActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCreateDonorReqBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCreateDonorReqBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btBack.setOnClickListener {
            finish()
        }
    }
}