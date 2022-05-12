package com.example.redminecapstoneproject.ui.detaildonorreq

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ActivityDetailDonorRequestBinding
import com.example.redminecapstoneproject.databinding.ActivityDonorRequestBinding

class DetailDonorRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDonorRequestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailDonorRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btBack.setOnClickListener {
            finish()
        }
    }
}