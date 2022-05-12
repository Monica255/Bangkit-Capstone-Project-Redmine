package com.example.redminecapstoneproject.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationBarView


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController=findNavController(R.id.frame_container)
        binding.navigation.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home ->{
                    navController.navigate(R.id.action_profileFragment_to_homeFragment)
                    return@OnItemSelectedListener true
                }
                R.id.menu_profile -> {
                    navController.navigate(R.id.action_homeFragment_to_profileFragment)
                    return@OnItemSelectedListener true
                }
            }
            false
        })


    }
}