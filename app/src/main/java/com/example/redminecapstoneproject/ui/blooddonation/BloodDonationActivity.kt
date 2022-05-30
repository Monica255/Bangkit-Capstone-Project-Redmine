package com.example.redminecapstoneproject.ui.blooddonation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.adapter.HorizontalDonorReqAdapter
import com.example.redminecapstoneproject.databinding.ActivityBloodDonationBinding
import com.example.redminecapstoneproject.helper.helperBloodDonors
import com.example.redminecapstoneproject.helper.helperDate
import com.example.redminecapstoneproject.ui.DonationEventsActivity
import com.example.redminecapstoneproject.ui.createdonorreq.CreateDonorReqActivity
import com.example.redminecapstoneproject.ui.detaildonorreq.DetailDonorRequestActivity
import com.example.redminecapstoneproject.ui.loginsignup.LoginSignupViewModel
import com.example.redminecapstoneproject.ui.profile.DonorDetailActivity
import com.example.redminecapstoneproject.ui.testing.DonorDataRoom
import com.example.redminecapstoneproject.ui.testing.DonorRequest
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate

class BloodDonationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBloodDonationBinding
    private lateinit var province: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBloodDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val loginSignupViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[LoginSignupViewModel::class.java]

        val bloodDonationViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[BloodDonationViewModel::class.java]


        binding.cvDonorDetail.setOnClickListener {
            startActivity(Intent(this, DonorDetailActivity::class.java))
        }


        binding.btBloodDonationEvents.setOnClickListener {
            startActivity(Intent(this, DonationEventsActivity::class.java))
        }

        binding.btBloodDonorReq.setOnClickListener {
            startActivity(Intent(this, DonorRequestActivity::class.java))
        }

        binding.btBack.setOnClickListener {
            finish()
        }

        binding.btBloodDonorReq.setOnClickListener {
            val intent = Intent(this, DonorRequestActivity::class.java)
            startActivity(intent)
        }

        loginSignupViewModel.getUserDonorDataDb().observe(this) {
            province = it.province.toString()
            setData(it)

            bloodDonationViewModel.getDonorReq()
            bloodDonationViewModel.donorReq.observe(this) { it2 ->
                if (it2 != null) {
                    setAdapterDonorReq(it2)
                }
            }
        }



        bloodDonationViewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }

    private fun showLoading(show: Boolean) {

        if (show) {
            binding.lazyLoading.visibility = View.VISIBLE
            binding.content.visibility = View.GONE
        } else {
            binding.lazyLoading.visibility = View.GONE
            binding.content.visibility = View.VISIBLE
        }
    }

    private fun setAdapterDonorReq(list: List<DonorRequest>) {
        var s = ArrayList<DonorRequest>()
        var counter = 0
        first@ for (i in list) {
            if (i.province == province) {
                s.add(i)
                counter++
            }
            if (counter == 5) break@first
        }
        if (s.isEmpty()) {
            binding.ltNoDataDonorReq.visibility = View.VISIBLE
            binding.rvHorizontalDonorRequest.alpha = 0F
        } else {
            binding.ltNoDataDonorReq.visibility = View.GONE
            binding.rvHorizontalDonorRequest.alpha = 1F
        }
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHorizontalDonorRequest.layoutManager = layoutManager

        val mAdapter = HorizontalDonorReqAdapter(s)
        binding.rvHorizontalDonorRequest.adapter = mAdapter

        mAdapter.setOnItemClickCallback(object : HorizontalDonorReqAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DonorRequest) {
                if (data.uid == FirebaseAuth.getInstance().currentUser?.uid) {
                    val intent =
                        Intent(this@BloodDonationActivity, CreateDonorReqActivity::class.java)
                    intent.putExtra(CreateDonorReqActivity.EXTRA_DONOR_REQ, data)
                    startActivity(intent)
                } else {
                    val intent = Intent(
                        this@BloodDonationActivity,
                        DetailDonorRequestActivity::class.java
                    )
                    intent.putExtra(DetailDonorRequestActivity.EXTRA_DONOR_REQ, data)
                    startActivity(intent)
                }

            }

        })

    }

    @SuppressLint("StringFormatMatches")
    private fun setData(data: DonorDataRoom) {
        if (data != null) {

            val mLastDOnate =
                if (data?.lastDonateDate != null) helperDate.stringToDate(data.lastDonateDate!!) else null


            binding.tvVerifyState.text =
                if (data.isVerified) resources.getString(R.string.verified_account) else getString(
                    R.string.unverified_account
                )
            if (mLastDOnate != null) {
                binding.lastBloodDonation.text = getString(
                    R.string.date_format,
                    mLastDOnate!!.month,
                    mLastDOnate!!.dayOfMonth,
                    mLastDOnate!!.year
                )

                val x: LocalDate = helperDate.canDonateAgain(mLastDOnate!!)
                binding.canDonateAgain.text = getString(
                    R.string.date_format,
                    x.month,
                    x.dayOfMonth,
                    x.year
                )
            } else {
                binding.lastBloodDonation.text = "--"
                binding.canDonateAgain.text = "--"
            }
            binding.tvBloodType.text = helperBloodDonors.toBloodType(data.bloodType, data.rhesus)
        }
    }

}