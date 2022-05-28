package com.example.redminecapstoneproject.ui.blooddonation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.adapter.BloodDonorsAdapter
import com.example.redminecapstoneproject.databinding.ActivityBloodDonorsBinding
import com.example.redminecapstoneproject.helper.helperBloodDonors
import com.example.redminecapstoneproject.ui.testing.BloodDonors
import com.google.firebase.auth.FirebaseAuth

class BloodDonorsActivity : AppCompatActivity() {
    private lateinit var bindind:ActivityBloodDonorsBinding
    private lateinit var listValidDOnors:List<BloodDonors>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindind= ActivityBloodDonorsBinding.inflate(layoutInflater)
        setContentView(bindind.root)
        val bloodDonationViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[BloodDonationViewModel::class.java]

        bloodDonationViewModel.getAllVerifiedUsers()
        bloodDonationViewModel.getAllVerifiedDonorDataUsers()

        bloodDonationViewModel.validAccUsers.observe(this){value->
            if(value!=null){
                bloodDonationViewModel.validDonorDataUsers.observe(this){
                    if(it!=null){
                        FirebaseAuth.getInstance().currentUser?.uid?.let { it1 ->
                            listValidDOnors=helperBloodDonors.toValidBloodDonorsList(value,it,
                                it1
                            )
                        }
                        setAdapter(listValidDOnors)
                        Log.d("DONATION",it.toString())
                    }
                }
            }
        }

        bindind.btBack.setOnClickListener {
            finish()
        }

    }


    private fun setAdapter(list:List<BloodDonors>){
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        bindind.rvBloodDonors.layoutManager = layoutManager
        val mAdaper= BloodDonorsAdapter(list)
        bindind.rvBloodDonors.adapter=mAdaper

        mAdaper.setOnItemClickCallback(object :BloodDonorsAdapter.OnItemClickCallback{
            override fun onItemClicked(data: BloodDonors) {

            }

        })

    }
}