package com.example.redminecapstoneproject.ui.mydonorreq

import android.content.Context
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.adapter.BloodDonorsAdapter
import com.example.redminecapstoneproject.adapter.DonorReqAdapter
import com.example.redminecapstoneproject.databinding.ActivityMyDonorReqBinding
import com.example.redminecapstoneproject.ui.blooddonation.BloodDonationViewModel
import com.example.redminecapstoneproject.ui.testing.BloodDonors
import com.example.redminecapstoneproject.ui.testing.DonorDataRoom
import com.example.redminecapstoneproject.ui.testing.DonorRequest

class MyDonorReqActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyDonorReqBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMyDonorReqBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val myDonorReqViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[MyDonorReqViewModel::class.java]


        myDonorReqViewModel.getMyDonorReq()



        myDonorReqViewModel.donorReq.observe(this){
            if(it!=null){
                setAdapter(it)
            }
        }



        binding.btBack.setOnClickListener {
            onBackPressed()
        }

    }


    private fun setAdapter(list:List<DonorRequest>){
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvBloodDonors.layoutManager = layoutManager
        val mAdaper= DonorReqAdapter(list)
        binding.rvBloodDonors.adapter=mAdaper

        mAdaper.setOnItemClickCallback(object : DonorReqAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DonorRequest) {

            }

        })

    }
}