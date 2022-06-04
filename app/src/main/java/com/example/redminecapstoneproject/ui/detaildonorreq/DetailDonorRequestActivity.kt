package com.example.redminecapstoneproject.ui.detaildonorreq

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.databinding.ActivityDetailDonorRequestBinding
import com.example.redminecapstoneproject.helper.HelperBloodDonors
import com.example.redminecapstoneproject.helper.HelperDate
import com.example.redminecapstoneproject.helper.HelperUserDetail
import com.example.redminecapstoneproject.ui.testing.DonorRequest

class DetailDonorRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDonorRequestBinding
    private var donorReq:DonorRequest?=DonorRequest()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDonorRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)


        donorReq = intent.getParcelableExtra(EXTRA_DONOR_REQ)

        val detailDonorReqViewModel=ViewModelProvider(this)[DetailDonorReqViewModel::class.java]
        detailDonorReqViewModel.phoneNumber= donorReq?.phoneNumber.toString()

        donorReq?.let { setData(it) }

        binding.btBack.setOnClickListener {
            onBackPressed()
        }


    }

    private fun setData(data: DonorRequest) {
        binding.apply {
            tvPostTime.text = data.time?.let { HelperDate.toPostTime(it, baseContext) }
            tvDonorReqPatientName.text = data.patientName
            tvDonorReqBloodBagNeeded.text = data.numberOfBloodBag?.let {
                HelperBloodDonors.toBagsFormat(
                    it
                )
            }
            tvLocation.text = HelperBloodDonors.toLocation(
                HelperUserDetail.toTitleCase(data.city),
                HelperUserDetail.toTitleCase(data.province?.let {
                    HelperUserDetail.getProvinceName(it)
                })

            )
            tvDonorReqHospitalName.text = data.hospitalName
            tvContactName.text = data.contactName
            tvDonorReqDes.text=data.description?:""
            tvBloodType.text = HelperBloodDonors.toBloodType(data.bloodType, data.rhesus)

            tvCompatibility.text=intent.getStringExtra(EXTRA_COMP)
            tvCompatibility.setTextColor(intent.getIntExtra(EXTRA_COMP_COLOR,0))
        }
    }

    companion object {
        const val EXTRA_DONOR_REQ = "donor_request"
        const val EXTRA_COMP="extra_comp"
        const val EXTRA_COMP_COLOR="extra_comp_color"
    }

}