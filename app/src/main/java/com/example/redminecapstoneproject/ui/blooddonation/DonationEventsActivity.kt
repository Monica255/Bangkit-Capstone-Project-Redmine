package com.example.redminecapstoneproject.ui.blooddonation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redminecapstoneproject.CustomDialogFragment
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.adapter.VerticalDonorEventAdapter
import com.example.redminecapstoneproject.adapter.VerticalDonorReqAdapter
import com.example.redminecapstoneproject.databinding.ActivityDonationEventsBinding
import com.example.redminecapstoneproject.helper.HelperBloodDonors
import com.example.redminecapstoneproject.helper.HelperDate
import com.example.redminecapstoneproject.helper.HelperUserDetail
import com.example.redminecapstoneproject.ui.createdonorreq.CreateDonorReqActivity
import com.example.redminecapstoneproject.ui.detaildonorreq.DetailDonorRequestActivity
import com.example.redminecapstoneproject.ui.loginsignup.LoginSignupViewModel
import com.example.redminecapstoneproject.ui.profile.DonorDetailActivity
import com.example.redminecapstoneproject.ui.profile.UserDetailViewModel
import com.example.redminecapstoneproject.ui.testing.DonorDataRoom
import com.example.redminecapstoneproject.ui.testing.DonorEvent
import com.example.redminecapstoneproject.ui.testing.DonorRequest
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate

class DonationEventsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDonationEventsBinding
    private lateinit var province: String
    private lateinit var city: String
    private lateinit var filterCity: String
    private var arg = Bundle()

    private fun newDialog(title: String): CustomDialogFragment {
        val dialog = CustomDialogFragment()
        dialog.show(supportFragmentManager, "mDialog")
        arg.putString(DonorRequestActivity.EXTRA_TITLE, title)
        dialog.arguments
        dialog.arguments = arg
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonationEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loginSignupViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[LoginSignupViewModel::class.java]

        val userDetailViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[UserDetailViewModel::class.java]


        val bloodDonationViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[BloodDonationViewModel::class.java]

        binding.cvDonorDetail.setOnClickListener {
            startActivity(Intent(this, DonorDetailActivity::class.java))
        }

        loginSignupViewModel.getUserDonorDataDb().observe(this) {
            province = it.province.toString()
            city = it.city.toString()
            filterCity = city
            binding.btFilter.text = HelperUserDetail.toTitleCase(city)
            userDetailViewModel.getCities(
                HelperUserDetail.getProvinceID(province)
            )
            setData(it)
            Log.d("LOADING", "yy $it")

            bloodDonationViewModel.getDonationEvent()
            bloodDonationViewModel.filterCity.value = filterCity
            bloodDonationViewModel.donationEvent.observe(this) { it2 ->
                if (it2 != null) {
                    Log.d("LOADING", "xx $it2")

                    bloodDonationViewModel.filterCity.observe(this) { v ->
                        setAdapter(filterList(v, it2))
                        //Log.d("LOADING", "zz $v"+ filterList(v, it2))
                    }
                }
            }
        }

        binding.btFilter.setOnClickListener {
            newDialog(CustomDialogFragment.FILTER_BY_CITY)
        }

        binding.btBack.setOnClickListener {
            finish()
        }
    }

    private fun filterList(filterCity: String, list: List<DonorEvent>): List<DonorEvent> {
        val x = ArrayList<DonorEvent>()
        for (i in list) {
            if (i.city.equals(filterCity, true)) {
                x.add(i)
            }
        }
        return x
    }

    private fun setAdapter(list: List<DonorEvent>) {
        Log.d("EVT",list.toString())
        if (list.isEmpty()) {
            binding.ltNoData.visibility = View.VISIBLE
            binding.rvDonationEvents.visibility = View.GONE
        } else {
            binding.ltNoData.visibility = View.GONE
            binding.rvDonationEvents.visibility = View.VISIBLE
        }
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDonationEvents.layoutManager = layoutManager

        val mAdaper = VerticalDonorEventAdapter(list)
        binding.rvDonationEvents.adapter = mAdaper
        mAdaper.setOnItemClickCallback(object : VerticalDonorEventAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DonorEvent) {

                val intent =
                    Intent(this@DonationEventsActivity, DetailDonationEventsActivity::class.java)
                intent.putExtra(DetailDonationEventsActivity.EXTRA_DONATION_EVENT, data)
                startActivity(intent)


            }

        })

    }


    @SuppressLint("StringFormatMatches")
    private fun setData(data: DonorDataRoom) {

        val mLastDOnate =
            if (data.lastDonateDate != null) HelperDate.stringToDate(data.lastDonateDate!!) else null


        binding.tvVerifyState.text =
            if (data.isVerified) resources.getString(R.string.verified_account) else getString(
                R.string.unverified_account
            )
        if (mLastDOnate != null) {
            binding.lastBloodDonation.text = getString(
                R.string.date_format,
                HelperDate.monthToString(mLastDOnate.month,baseContext),
                mLastDOnate.dayOfMonth,
                mLastDOnate.year
            )

            val x: LocalDate = HelperDate.canDonateAgain(mLastDOnate)
            binding.canDonateAgain.text = getString(
                R.string.date_format,
                HelperDate.monthToString(x.month,baseContext),
                x.dayOfMonth,
                x.year
            )
        } else {
            binding.lastBloodDonation.text = "--"
            binding.canDonateAgain.text = "--"
        }
        binding.tvBloodType.text = HelperBloodDonors.toBloodType(data.bloodType, data.rhesus)
    }

}