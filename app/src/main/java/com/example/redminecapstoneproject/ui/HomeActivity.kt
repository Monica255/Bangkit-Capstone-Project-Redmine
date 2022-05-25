package com.example.redminecapstoneproject.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.ActivityHomeBinding
import com.example.redminecapstoneproject.ui.loginsignup.LoginSignupViewModel
import com.google.android.material.navigation.NavigationBarView


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val navHostFragment = findNavController(R.id.frame_container) as NavHostFragment
        val navController = findNavController(R.id.frame_container)

        binding.navigation.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    try{
                        navController.navigate(R.id.action_profileFragment_to_homeFragment)
                        Log.d("TAG", "ha " + navController.currentDestination?.id.toString())
                    }catch (e:Exception){
                        Log.e("ERROR","Already in destination")
                    }


                    return@OnItemSelectedListener true
                }
                R.id.menu_profile -> {
                    try {
                        navController.navigate(R.id.action_homeFragment_to_profileFragment)
                        Log.d("TAG", navController.currentDestination?.id.toString())
                    }catch (e:java.lang.Exception){
                        Log.e("ERROR","Already in destination")
                    }

                    return@OnItemSelectedListener true
                }
            }
            false
        })


    }


}