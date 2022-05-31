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
import com.example.redminecapstoneproject.adapter.HorizontalDonorReqAdapter
import com.example.redminecapstoneproject.adapter.VerticalDonorReqAdapter
import com.example.redminecapstoneproject.databinding.ActivityDonorRequestBinding
import com.example.redminecapstoneproject.helper.helperBloodDonors
import com.example.redminecapstoneproject.helper.helperDate
import com.example.redminecapstoneproject.helper.helperUserDetail
import com.example.redminecapstoneproject.ui.createdonorreq.CreateDonorReqActivity
import com.example.redminecapstoneproject.ui.detaildonorreq.DetailDonorRequestActivity
import com.example.redminecapstoneproject.ui.loginsignup.LoginSignupViewModel
import com.example.redminecapstoneproject.ui.profile.DonorDetailActivity
import com.example.redminecapstoneproject.ui.profile.UserDetailViewModel
import com.example.redminecapstoneproject.ui.testing.BloodDonors
import com.example.redminecapstoneproject.ui.testing.DonorDataRoom
import com.example.redminecapstoneproject.ui.testing.DonorRequest
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate

class DonorRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDonorRequestBinding
    private lateinit var province: String
    private lateinit var city: String
    private lateinit var bloodType:String
    private var arg = Bundle()

    private fun newDialog(title: String): CustomDialogFragment {
        val dialog = CustomDialogFragment()
        dialog.show(supportFragmentManager, "mDialog")
        arg.putString("title", title)
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
            bloodType=helperBloodDonors.toBloodType(it.bloodType,it.rhesus)
            binding.btFilter.text = city?.lowercase()?.replaceFirstChar(Char::titlecase)
            userDetailViewModel.getCities(
                helperUserDetail.getProvinceID(province)
            )
            setData(it)
            Log.d("LOADING", "yy " + it.toString())

            bloodDonationViewModel.getDonorReq()
            bloodDonationViewModel.donorReq.observe(this) { it2 ->
                if (it2 != null) {
                    //setAdapter(it2)
                    Log.d("LOADING", "xx " + it2.toString())
                    bloodDonationViewModel.filterCity.value = city
                    //bloodDonationViewModel.filterProvince=province
                    bloodDonationViewModel.filterCity.observe(this) { it ->
                        setAdapter(filterList(it, it2),bloodType)
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


    fun filterList(filterCity: String, list: List<DonorRequest>): List<DonorRequest> {
        var x = ArrayList<DonorRequest>()
        for (i in list) {
            if (i.city == filterCity) {
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
                    val bt=helperBloodDonors.toBloodType(data.bloodType,data.rhesus)
                    val comp=helperBloodDonors.checkCompatibility(mBloodType,bt,this@DonorRequestActivity)
                    val compColor=helperBloodDonors.setColor(comp,this@DonorRequestActivity)
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