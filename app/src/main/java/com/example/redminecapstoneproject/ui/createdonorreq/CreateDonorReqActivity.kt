package com.example.redminecapstoneproject.ui.createdonorreq

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ActivityCreateDonorReqBinding
import com.example.redminecapstoneproject.ui.donordata.DonorDataViewModel

class CreateDonorReqActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateDonorReqBinding
    private var isPatientNameValid: Boolean = false
        get() {
            checkPatientName()
            return field
        }
    private var isNumberOfBloodBagValid: Boolean = false
        get(){
            checkNumberOfBloodBag()
            return field
        }
    private var isBloodTypeValid = false
        get() {
            checkBloodType()
            return field
        }
    private var isRhesusValid = false
        get() {
            checkRhesus()
            return field
        }
    private var isProvinceValid = false
        get() {
            field = binding.tvProvince.text.toString().trim() != "Province"
            return field
        }
    private var isCityValid = false
        get() {
            field = binding.tvCity.text.toString().trim() != "City"
            return field
        }
    private var isHospitalNameValid = false

    private var isDescriptionValid = false
    private var isContactNameValid = false
    private var isPhoneNumberValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateDonorReqBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btBack.setOnClickListener {
            finish()
        }


    }

    private fun isFieldsEmpty(): Boolean {
        return binding.tvProvince.text.toString().trim() == "Province"
                && binding.tvCity.text.toString().trim() == "City"
                && binding.rgBloodType.checkedRadioButtonId == -1
                && binding.rgRhesus.checkedRadioButtonId == -1
                && binding.etPatientName.text.toString().trim() == ""
                && binding.etNumberOfBloodNeeded.text.toString().trim() == ""
                && binding.etHospitalName.text.toString().trim() == ""
                && binding.etDescription.text.toString().trim() == ""
                && binding.etPhoneNumber.text.toString().trim() == ""
    }

    private fun isDataValid(): Boolean {
        Log.d("TAG", isPatientNameValid.toString())
        Log.d("TAG", isNumberOfBloodBagValid.toString())
        Log.d("TAG", isBloodTypeValid.toString())
        Log.d("TAG", isRhesusValid.toString())
        Log.d("TAG", isProvinceValid.toString())
        Log.d("TAG", isCityValid.toString())
        Log.d("TAG", isHospitalNameValid.toString())
        Log.d("TAG", isDescriptionValid.toString())
        Log.d("TAG", isContactNameValid.toString())
        Log.d("TAG", isPhoneNumberValid.toString())

        return isPatientNameValid && isNumberOfBloodBagValid && isBloodTypeValid && isRhesusValid && isProvinceValid && isCityValid && isHospitalNameValid && isDescriptionValid && isContactNameValid && isPhoneNumberValid
    }

    private fun checkPatientName() {
        val createDonorReqViewModel =
            ViewModelProvider(this)[CreateDonorReqViewModel::class.java]
        val name = binding.etPatientName.text.toString().trim()
        if (name.isEmpty()) {
            isPatientNameValid = false
            binding.ilPatientName.error = getString(R.string.name_required)
        } else {
            isPatientNameValid = true
            //createDonorReqViewModel.donorReq.patientName = name
        }
    }

    private fun checkNumberOfBloodBag() {
        val createDonorReqViewModel =
            ViewModelProvider(this)[CreateDonorReqViewModel::class.java]
        val number = binding.etNumberOfBloodNeeded.text.toString().trim()
        if (number.isEmpty()) {
            isNumberOfBloodBagValid = false
            binding.ilNumberOfBloodNeeded.error = getString(R.string.name_required)
        } else if (number.toInt() > 5) {
            isNumberOfBloodBagValid = false
            binding.ilNumberOfBloodNeeded.error = getString(R.string.max_blood_bag)
        } else {
            isNumberOfBloodBagValid = true
            //createDonorReqViewModel.donorReq.phoneNumber = number
        }
    }

    private fun checkBloodType() {
        val createDonorReqViewModel =
            ViewModelProvider(this)[CreateDonorReqViewModel::class.java]
        val selectedBloodType = binding.rgBloodType.checkedRadioButtonId
        if (selectedBloodType == -1) {
            isBloodTypeValid = false
        } else {
            val radio: RadioButton = this.findViewById(selectedBloodType)
            isBloodTypeValid = true
            //createDonorReqViewModel.donorReq.bloodType = radio.text.toString().lowercase()
        }
    }

    private fun checkRhesus() {
        val createDonorReqViewModel =
            ViewModelProvider(this)[CreateDonorReqViewModel::class.java]
        val selectedRhesus = binding.rgRhesus.checkedRadioButtonId
        if (selectedRhesus == -1) {
            isRhesusValid = false
        } else {
            val radio: RadioButton = this.findViewById(selectedRhesus)
            isRhesusValid = true
            //createDonorReqViewModel.donorReq.rhesus = radio.text.toString().lowercase()
        }
    }

    private fun checkHospitalName() {
        val createDonorReqViewModel =
            ViewModelProvider(this)[CreateDonorReqViewModel::class.java]
        val name = binding.etHospitalName.text.toString().trim()
        if (name.isEmpty()) {
            isHospitalNameValid = false
            binding.ilHospitalName.error = getString(R.string.hospital_name_required)
        } else {
            isHospitalNameValid = true
            //createDonorReqViewModel.donorReq.hospitalName = name
        }
    }

    private fun checkDescription() {
        val createDonorReqViewModel =
            ViewModelProvider(this)[CreateDonorReqViewModel::class.java]
        val selectedPhoneNumber = binding.etDescription.text.toString().trim()
        if (selectedPhoneNumber.isEmpty()) {
            isDescriptionValid = false
            binding.ilDescription.error = getString(R.string.des_required)
        } else if (selectedPhoneNumber.length > 200) {
            isDescriptionValid = false
            binding.ilDescription.error = getString(R.string.des_max_length)
        } else
            isDescriptionValid = true
        //createDonorReqViewModel.donorReq.description = selectedPhoneNumber

    }

    private fun checkContactName() {
        val createDonorReqViewModel =
            ViewModelProvider(this)[CreateDonorReqViewModel::class.java]
        val name = binding.etContactName.text.toString().trim()
        if (name.isEmpty()) {
            isContactNameValid = false
            binding.ilContactName.error = getString(R.string.name_required)
        } else {
            isContactNameValid = true
            //createDonorReqViewModel.donorReq.contactName = name
        }
    }

    private fun checkPhoneNumber() {
        val createDonorReqViewModel =
            ViewModelProvider(this)[CreateDonorReqViewModel::class.java]
        val selectedPhoneNumber = binding.etPhoneNumber.text.toString().trim()
        if (selectedPhoneNumber.isEmpty()) {
            isPhoneNumberValid = false
            binding.ilPhone.error = getString(R.string.input_phone_number)
        } else if (selectedPhoneNumber.length < 9) {
            isPhoneNumberValid = false
            binding.ilPhone.error = getString(R.string.phone_number_length_invalid)
        } else
            isPhoneNumberValid = true
        //createDonorReqViewModel.donorReq.phoneNumber = selectedPhoneNumber
    }
}