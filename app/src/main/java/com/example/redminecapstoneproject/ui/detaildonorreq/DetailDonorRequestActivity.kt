package com.example.redminecapstoneproject.ui.detaildonorreq

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.databinding.ActivityDetailDonorRequestBinding
import com.example.redminecapstoneproject.helper.helperBloodDonors
import com.example.redminecapstoneproject.helper.helperDate
import com.example.redminecapstoneproject.helper.helperUserDetail
import com.example.redminecapstoneproject.ui.testing.DonorRequest

class DetailDonorRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDonorRequestBinding
    private var donorReq:DonorRequest?=DonorRequest()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDonorRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)


        donorReq = intent.getParcelableExtra<DonorRequest>(EXTRA_DONOR_REQ)

        var detailDonorReqViewModel=ViewModelProvider(this)[DetailDonorReqViewModel::class.java]
        detailDonorReqViewModel.phoneNumber= donorReq?.phoneNumber.toString()

        donorReq?.let { setData(it) }

        binding.btBack.setOnClickListener {
            onBackPressed()
        }


    }

    private fun setData(data: DonorRequest) {
        binding.apply {
            tvPostTime.text = data.time?.let { helperDate.toPostTime(it, baseContext) }
            tvDonorReqPatientName.text = data.patientName
            tvDonorReqBloodBagNeeded.text = data.numberOfBloodBag?.let {
                helperBloodDonors.toBagsFormat(
                    it
                )
            }
            tvLocation.text = helperBloodDonors.toLocation(
                helperUserDetail.toTitleCase(data.city),
                helperUserDetail.toTitleCase(data.province?.let {
                    helperUserDetail.getProvinceName(it)
                })

            )
            tvDonorReqHospitalName.text = data.hospitalName
            tvContactName.text = data.contactName
            tvDonorReqDes.text=data.description?:""
            tvBloodType.text = helperBloodDonors.toBloodType(data.bloodType, data.rhesus)
        }
    }

    companion object {
        const val EXTRA_DONOR_REQ = "donor_request"
    }

}