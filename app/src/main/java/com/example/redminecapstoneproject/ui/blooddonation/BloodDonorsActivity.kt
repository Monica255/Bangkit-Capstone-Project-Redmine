package com.example.redminecapstoneproject.ui.blooddonation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redminecapstoneproject.CustomDialogFragment
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.adapter.BloodDonorsAdapter
import com.example.redminecapstoneproject.databinding.ActivityBloodDonorsBinding
import com.example.redminecapstoneproject.helper.helperBloodDonors
import com.example.redminecapstoneproject.helper.helperUserDetail
import com.example.redminecapstoneproject.ui.BloodDonorDetailsActivity
import com.example.redminecapstoneproject.ui.profile.UserDetailViewModel
import com.example.redminecapstoneproject.ui.testing.BloodDonors
import com.google.firebase.auth.FirebaseAuth

class BloodDonorsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBloodDonorsBinding
    private lateinit var listValidDOnors: List<BloodDonors>
    private lateinit var province: String
    private lateinit var city: String
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
        binding = ActivityBloodDonorsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bloodDonationViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[BloodDonationViewModel::class.java]

        val userDetailViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[UserDetailViewModel::class.java]

        bloodDonationViewModel.getAllVerifiedUsers()
        bloodDonationViewModel.getAllVerifiedDonorDataUsers()
        province = intent.getStringExtra(EXTRA_PROVINCE).toString()
        city = intent.getStringExtra(EXTRA_CITY).toString()
        binding.btFilter.text = city?.lowercase()?.replaceFirstChar(Char::titlecase)

        userDetailViewModel.getCities(
            helperUserDetail.getProvinceID(province)
        )

        bloodDonationViewModel.validAccUsers.observe(this) { value ->
            if (value != null) {
                bloodDonationViewModel.validDonorDataUsers.observe(this) {
                    if (it != null) {
                        FirebaseAuth.getInstance().currentUser?.uid?.let { it1 ->
                            listValidDOnors = helperBloodDonors.toValidBloodDonorsList(
                                value, it,
                                it1
                            )
                        }
                        bloodDonationViewModel.filterCity.value = city
                        //bloodDonationViewModel.filterProvince=province
                        bloodDonationViewModel.filterCity.observe(this) {
                            setAdapter(filterList(it, listValidDOnors))
                        }
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

        bloodDonationViewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }

    private fun showLoading(show: Boolean) {

        if (show) {
            binding.ltJavrvis.visibility = View.VISIBLE
            binding.rvBloodDonors.visibility = View.GONE
        } else {
            binding.ltJavrvis.visibility = View.GONE
            binding.rvBloodDonors.visibility = View.VISIBLE
        }
    }

    fun filterList(filterCity: String, list: List<BloodDonors>): List<BloodDonors> {
        var x = ArrayList<BloodDonors>()
        for (i in list) {
            if (i.city == filterCity) {
                x.add(i)
            }
        }
        return x
    }


    private fun setAdapter(list: List<BloodDonors>) {

        if (list.isEmpty()) {
            binding.ltNoData.visibility = View.VISIBLE
            binding.rvBloodDonors.visibility = View.GONE
        } else {
            binding.ltNoData.visibility = View.GONE
            binding.rvBloodDonors.visibility = View.VISIBLE
        }
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvBloodDonors.layoutManager = layoutManager
        val mAdaper = BloodDonorsAdapter(list)
        binding.rvBloodDonors.adapter = mAdaper

        mAdaper.setOnItemClickCallback(object : BloodDonorsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: BloodDonors) {
                var intent =
                    Intent(this@BloodDonorsActivity, BloodDonorDetailsActivity::class.java)
                intent.putExtra(EXTRA_BLOOD_DONOR, data)
                startActivity(intent)
            }

        })


    }

    companion object {
        const val EXTRA_PROVINCE = "extra_province"
        const val EXTRA_CITY = "extra_city"
        const val EXTRA_BLOOD_DONOR = "extra_blood_donor"
    }
}