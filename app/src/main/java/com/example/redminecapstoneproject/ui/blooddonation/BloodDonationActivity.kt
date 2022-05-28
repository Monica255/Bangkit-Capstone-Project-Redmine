package com.example.redminecapstoneproject.ui.blooddonation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.ActivityBloodDonationBinding
import com.example.redminecapstoneproject.helper.helperDate
import com.example.redminecapstoneproject.ui.DonationEventsActivity
import com.example.redminecapstoneproject.ui.DonorRequestActivity
import com.example.redminecapstoneproject.ui.loginsignup.LoginSignupViewModel
import com.example.redminecapstoneproject.ui.profile.DonorDetailActivity
import com.example.redminecapstoneproject.ui.testing.DonorDataRoom
import java.time.LocalDate

class BloodDonationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBloodDonationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBloodDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Log.d("DONATION",System.currentTimeMillis().toString())

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

        loginSignupViewModel.getUserDonorDataDb().observe(this){
            setData(it)
        }

    }


    @SuppressLint("StringFormatMatches")
    private fun setData(data: DonorDataRoom) {
        Log.d("DONATION", data.toString())
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


        }
    }

}