package com.example.redminecapstoneproject.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ActivityUserDetailBinding

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBack.setOnClickListener{
            finish()
        }
    }
}