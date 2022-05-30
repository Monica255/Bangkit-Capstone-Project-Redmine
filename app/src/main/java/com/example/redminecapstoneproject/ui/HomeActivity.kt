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
    lateinit var state:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val navHostFragment = findNavController(R.id.frame_container) as NavHostFragment
        val navController = findNavController(R.id.frame_container)

        binding.navigation.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    Log.d("STATE","home click from "+state)
                    if(state== FAQ){
                        navController.navigate(R.id.action_faqFragment_to_homeFragment)
                    }else if(state== PROFILE) {
                        navController.navigate(R.id.action_profileFragment_to_homeFragment)
                    }
                    Log.d("STATE","now state is "+state)
                    return@OnItemSelectedListener true
                }
                R.id.menu_profile -> {
                    Log.d("STATE","profile click from "+state)
                    if(state== HOME){
                        navController.navigate(R.id.action_homeFragment_to_profileFragment)
                    }else if(state== FAQ) {
                        navController.navigate(R.id.action_faqFragment_to_profileFragment)
                    }
                    Log.d("STATE","now state is "+state)
                    return@OnItemSelectedListener true
                }

                R.id.menu_faq -> {
                    Log.d("STATE","faq click from "+state)
                    if(state== HOME){
                        navController.navigate(R.id.action_homeFragment_to_faqFragment)
                    }else if(state== PROFILE) {
                        navController.navigate(R.id.action_profileFragment_to_faqFragment)
                    }
                    Log.d("STATE","now state is "+state)
                    return@OnItemSelectedListener true
                }
            }
            false
        })


    }
    companion object{
        const val HOME="home"
        const val PROFILE="profile"
        const val FAQ="faq"
    }


}