package com.example.redminecapstoneproject.ui.donordata

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.ActivityDonorDataBinding


class DonorDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDonorDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonorDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val donorDataViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[DonorDataViewModel::class.java]

        val mFragmentManager = supportFragmentManager
        val mFirstDonorDataFragment = FirstDonorDataFragment()
        val fragment =
            mFragmentManager.findFragmentByTag(FirstDonorDataFragment::class.java.simpleName)

        donorDataViewModel.name= intent.getStringExtra("name").toString()

        binding.btBack.setOnClickListener {
            onBackPressed()
        }
    }
}
