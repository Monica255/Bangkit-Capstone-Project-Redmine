package com.example.redminecapstoneproject.ui.blooddonation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.bumptech.glide.Glide
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ActivityBloodDonorDetailsBinding
import com.example.redminecapstoneproject.helper.HelperBloodDonors
import com.example.redminecapstoneproject.helper.HelperUserDetail
import com.example.redminecapstoneproject.ui.testing.BloodDonors


class BloodDonorDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBloodDonorDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBloodDonorDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val donorData=intent.getParcelableExtra<BloodDonors>(BloodDonorsActivity.EXTRA_BLOOD_DONOR)
        if (donorData != null) {
            setData(donorData)
        }
        binding.btCopy.setOnClickListener {
            donorData?.phoneNumber?.let { it1 -> copyToClipBoard(it1) }
        }

        binding.btCall.setOnClickListener {
            donorData?.phoneNumber?.let { it1 -> dialPhoneNumber(it1) }
        }

        binding.btWa.setOnClickListener {
            donorData?.phoneNumber?.let { it1 -> openWhatsApp(it1) }
        }

        binding.btBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun openWhatsApp(phoneNumber: String){
        val validPhone=HelperUserDetail.toValidPhoneNumber(phoneNumber)
        val url = "https://wa.me/$validPhone"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun copyToClipBoard(phoneNumber: String){
        val validPhone=HelperUserDetail.toValidPhoneNumber(phoneNumber)
        val clipboard: ClipboardManager? =
            getSystemService(baseContext, ClipboardManager::class.java)
        val clip:ClipData?=ClipData.newPlainText(PHONE_NUMBER,validPhone)

        if(clip!=null){
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(this,getString(R.string.copied_to_clipboard),Toast.LENGTH_SHORT).show()
        }
    }

    private fun dialPhoneNumber(phoneNumber: String) {
        val validPhone= HelperUserDetail.toValidPhoneNumber(phoneNumber)
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$validPhone")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun setData(data:BloodDonors){
        binding.apply {
            tvDonorName.text=data.name
            tvDonorProvince.text= HelperUserDetail.toTitleCase(data.province?.let { HelperUserDetail.getProvinceName(it) })
            tvDonorCity.text=HelperUserDetail.toTitleCase(data.city)
            tvDonorGender.text=HelperUserDetail.toTitleCase(data.gender)
            tvDonorReqPhoneNumber.text=data.phoneNumber
            data.gender?.let { setAvatar(it) }
            tvBloodType.text=HelperBloodDonors.toBloodType(data.bloodType,data.rhesus)
        }
    }

    private fun setAvatar(gender: String) {
        val x: Int =
            if (gender == "male") R.drawable.img_profile_placeholder_male else R.drawable.img_profile_placeholder_female

        Glide.with(this)
            .load(x)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .fallback(R.drawable.ic_launcher_foreground)
            .into(binding.imgProfile)


    }

    companion object{
        const val PHONE_NUMBER="phone_number"
    }

}