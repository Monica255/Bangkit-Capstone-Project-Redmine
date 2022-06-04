package com.example.redminecapstoneproject.ui.blooddonation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redminecapstoneproject.CustomDialogFragment
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.adapter.VerticalDonorReqAdapter
import com.example.redminecapstoneproject.databinding.ActivityDonorRequestBinding
import com.example.redminecapstoneproject.helper.HelperBloodDonors
import com.example.redminecapstoneproject.helper.HelperDate
import com.example.redminecapstoneproject.helper.HelperUserDetail
import com.example.redminecapstoneproject.ui.createdonorreq.CreateDonorReqActivity
import com.example.redminecapstoneproject.ui.detaildonorreq.DetailDonorRequestActivity
import com.example.redminecapstoneproject.ui.loginsignup.LoginSignupViewModel
import com.example.redminecapstoneproject.ui.profile.DonorDetailActivity
import com.example.redminecapstoneproject.ui.profile.UserDetailViewModel
import com.example.redminecapstoneproject.ui.testing.DonorDataRoom
import com.example.redminecapstoneproject.ui.testing.DonorRequest
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate

class DonorRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDonorRequestBinding
    private lateinit var province: String
    private lateinit var city: String
    private lateinit var filterCity:String
    private lateinit var bloodType:String
    private var arg = Bundle()

    private fun newDialog(title: String): CustomDialogFragment {
        val dialog = CustomDialogFragment()
        dialog.show(supportFragmentManager, "mDialog")
        arg.putString(EXTRA_TITLE, title)
        dialog.arguments
        dialog.arguments = arg
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonorRequestBinding.inflate(layoutInflater)
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



        loginSignupViewModel.getUserDonorDataDb().observe(this) {
            province = it.province.toString()
            city = it.city.toString()
            filterCity=city
            bloodType=HelperBloodDonors.toBloodType(it.bloodType,it.rhesus)
            binding.btFilter.text = HelperUserDetail.toTitleCase(city)
            userDetailViewModel.getCities(
                HelperUserDetail.getProvinceID(province)
            )
            setData(it)
            Log.d("LOADING", "yy $it")

            bloodDonationViewModel.getDonorReq()
            bloodDonationViewModel.filterCity.value = filterCity
            bloodDonationViewModel.donorReq.observe(this) { it2 ->
                if (it2 != null) {
                    Log.d("LOADING", "xx $it2")

                    bloodDonationViewModel.filterCity.observe(this) { v ->
                        setAdapter(filterList(v, it2),bloodType)
                        Log.d("LOADING", "zz $v"+ filterList(v, it2))
                    }
                }
            }
        }


        binding.cvDonorDetail.setOnClickListener {
            startActivity(Intent(this, DonorDetailActivity::class.java))
        }

        binding.btFilter.setOnClickListener {
            newDialog(CustomDialogFragment.FILTER_BY_CITY)
        }

        binding.btBack.setOnClickListener {
            finish()
        }

        bloodDonationViewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }

    private fun showLoading(show: Boolean) {

        if (show) {
            binding.ltJavrvis.visibility = View.VISIBLE
            binding.rvDonorReq.visibility = View.GONE
        } else {
            binding.ltJavrvis.visibility = View.GONE
            binding.rvDonorReq.visibility = View.VISIBLE
        }
    }


    private fun filterList(filterCity: String, list: List<DonorRequest>): List<DonorRequest> {
        val x = ArrayList<DonorRequest>()
        for (i in list) {
            if (i.city.equals(filterCity,true)&& i.uid != FirebaseAuth.getInstance().currentUser?.uid) {
                x.add(i)
            }
        }
        return x
    }

    private fun setAdapter(list: List<DonorRequest>,mBloodType:String) {

        if (list.isEmpty()) {
            binding.ltNoData.visibility = View.VISIBLE
            binding.rvDonorReq.visibility = View.GONE
        } else {
            binding.ltNoData.visibility = View.GONE
            binding.rvDonorReq.visibility = View.VISIBLE
        }
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvDonorReq.layoutManager = layoutManager
        binding.rvDonorReq.adapter = null
        val mAdaper = VerticalDonorReqAdapter(list,mBloodType)
        binding.rvDonorReq.adapter = mAdaper
        mAdaper.setOnItemClickCallback(object : VerticalDonorReqAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DonorRequest) {
                if (data.uid == FirebaseAuth.getInstance().currentUser?.uid) {
                    val intent =
                        Intent(this@DonorRequestActivity, CreateDonorReqActivity::class.java)
                    intent.putExtra(CreateDonorReqActivity.EXTRA_DONOR_REQ, data)
                    startActivity(intent)
                } else {
                    val intent = Intent(
                        this@DonorRequestActivity,
                        DetailDonorRequestActivity::class.java
                    )
                    val bt=HelperBloodDonors.toBloodType(data.bloodType,data.rhesus)
                    val comp=HelperBloodDonors.checkCompatibility(mBloodType,bt,this@DonorRequestActivity)
                    val compColor=HelperBloodDonors.setColor(comp,this@DonorRequestActivity)
                    intent.putExtra(DetailDonorRequestActivity.EXTRA_DONOR_REQ, data)
                    intent.putExtra(DetailDonorRequestActivity.EXTRA_COMP, comp)
                    intent.putExtra(DetailDonorRequestActivity.EXTRA_COMP_COLOR, compColor)
                    startActivity(intent)
                }

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
                HelperDate.monthToString(mLastDOnate.month,this),
                mLastDOnate.dayOfMonth,
                mLastDOnate.year
            )

            val x: LocalDate = HelperDate.canDonateAgain(mLastDOnate)
            binding.canDonateAgain.text = getString(
                R.string.date_format,
                HelperDate.monthToString(x.month,this),
                x.dayOfMonth,
                x.year
            )
        } else {
            binding.lastBloodDonation.text = "--"
            binding.canDonateAgain.text = "--"
        }
        binding.tvBloodType.text = HelperBloodDonors.toBloodType(data.bloodType, data.rhesus)
    }


    companion object{
        const val EXTRA_TITLE="title"
    }
}