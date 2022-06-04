package com.example.redminecapstoneproject.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.ActivityHomeBinding
import com.example.redminecapstoneproject.repository.Repository
import com.google.android.material.navigation.NavigationBarView
import java.util.*


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    lateinit var state: String
    var funfact: String? = null
    var counter = 0

    override fun onStart() {

        if (counter == 0) {
            Log.d("EVT", "start " + counter)
            val sharedPref = this.getSharedPreferences("ff", Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putString("fun_fact", null)
                apply()
            }
        }
        counter = 1
        super.onStart()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = this.getSharedPreferences("ff", Context.MODE_PRIVATE) ?: return

        /*with (sharedPref.edit()) {
            putString("fun_fact", "ff")
            apply()
        }*/

        val homeViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[HomeViewModel::class.java]


        val navController = findNavController(R.id.frame_container)


        binding.navigation.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    if (state == FAQ) {
                        navController.navigate(R.id.action_faqFragment_to_homeFragment)
                    } else if (state == PROFILE) {
                        navController.navigate(R.id.action_profileFragment_to_homeFragment)
                    }
                    return@OnItemSelectedListener true
                }
                R.id.menu_profile -> {
                    if (state == HOME) {
                        navController.navigate(R.id.action_homeFragment_to_profileFragment)
                    } else if (state == FAQ) {
                        navController.navigate(R.id.action_faqFragment_to_profileFragment)
                    }
                    return@OnItemSelectedListener true
                }

                R.id.menu_faq -> {
                    if (state == HOME) {
                        navController.navigate(R.id.action_homeFragment_to_faqFragment)
                    } else if (state == PROFILE) {
                        navController.navigate(R.id.action_profileFragment_to_faqFragment)
                    }
                    return@OnItemSelectedListener true
                }

                R.id.menu_schedule -> {
                }
            }
            false
        })


    }


    companion object {
        const val HOME = "home"
        const val PROFILE = "profile"
        const val FAQ = "faq"
    }


}