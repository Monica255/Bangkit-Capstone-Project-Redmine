package com.example.redminecapstoneproject.ui.mydonorreq

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.adapter.DonorReqAdapter
import com.example.redminecapstoneproject.databinding.ActivityMyDonorReqBinding
import com.example.redminecapstoneproject.ui.createdonorreq.CreateDonorReqActivity
import com.example.redminecapstoneproject.ui.testing.DonorRequest
import java.util.*
import kotlin.collections.ArrayList

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
                Log.d("EMPTY",it.toString())
                setAdapter(it)
            }
        }

        binding.btBack.setOnClickListener {
            onBackPressed()
        }

        myDonorReqViewModel.isLoading.observe(this){
            Log.d("LOADING",it.toString())
            showLoading(it)
        }


    }
    private fun showLoading(show:Boolean){

        if(show){
            binding.ltJavrvis.visibility= View.VISIBLE
            binding.rvMyDonorReq.visibility= View.GONE
        }else{
            binding.ltJavrvis.visibility= View.GONE
            binding.rvMyDonorReq.visibility= View.VISIBLE
        }
    }

    private fun reverseListOrder(status: MutableList<DonorRequest>): List<DonorRequest>? {
        val it: MutableIterator<DonorRequest> = status.iterator()
        val destination: ArrayList<DonorRequest> = ArrayList()
        while (it.hasNext()) {
            destination.add(0, it.next())
            it.remove()
        }
        return destination
    }
    private fun setAdapter(list:List<DonorRequest>){

        if (list.isEmpty()) {
            binding.ltNoDataDonorReq.visibility = View.VISIBLE
            binding.rvMyDonorReq.visibility = View.GONE
        } else {
            binding.ltNoDataDonorReq.visibility = View.GONE
            binding.rvMyDonorReq.visibility = View.VISIBLE
        }
            val layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            binding.rvMyDonorReq.layoutManager = layoutManager

            binding.rvMyDonorReq.adapter = null
            Log.d("TESS", reverseListOrder(list.toMutableList()).toString())
            val mAdaper = DonorReqAdapter(reverseListOrder(list.toMutableList()))
            binding.rvMyDonorReq.adapter = mAdaper

            mAdaper.setOnItemClickCallback(object : DonorReqAdapter.OnItemClickCallback {
                override fun onItemClicked(data: DonorRequest) {

                    var intent = Intent(this@MyDonorReqActivity, CreateDonorReqActivity::class.java)
                    intent.putExtra(CreateDonorReqActivity.EXTRA_DONOR_REQ, data)
                    startActivity(intent)

                }

            })
        }



}

