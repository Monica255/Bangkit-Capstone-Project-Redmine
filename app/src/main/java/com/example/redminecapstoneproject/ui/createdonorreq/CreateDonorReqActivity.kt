package com.example.redminecapstoneproject.ui.createdonorreq

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.CustomDialogFragment
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ActivityCreateDonorReqBinding
import com.example.redminecapstoneproject.ui.donordata.DonorDataActivity
import com.example.redminecapstoneproject.ui.donordata.DonorDataViewModel
import www.sanju.motiontoast.MotionToast

class CreateDonorReqActivity : AppCompatActivity(), View.OnFocusChangeListener {
    private lateinit var binding: ActivityCreateDonorReqBinding
    private var arg = Bundle()
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
        get() {
            checkHospitalName()
            return field
        }
    private var isDescriptionValid = false
        get() {
            checkDescription()
            return field
        }
    private var isContactNameValid = false
        get() {
            checkContactName()
            return field
        }
    private var isPhoneNumberValid = false
        get() {
            checkPhoneNumber()
            return field
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateDonorReqBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btBack.setOnClickListener {
            finish()
        }

        binding.btPost.setOnClickListener {
            if (isDataValid()) {
                MotionToast.Companion.createColorToast(
                    this,
                    "Yey success 😍",
                    "Donor request is successfully posted!",
                    MotionToast.TOAST_SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(
                        this,
                        www.sanju.motiontoast.R.font.helvetica_regular
                    )
                )

                finish()
            } else {
                if (!isFieldsEmpty()) {
                    MotionToast.Companion.createColorToast(
                        this,
                        "Hey careful ",
                        "Please enter you data correctly",
                        MotionToast.TOAST_WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(
                            this,
                            www.sanju.motiontoast.R.font.helvetica_regular
                        )
                    )
                }
            }
        }

        binding.cvPickProvince.setOnClickListener {
            newDialog("Select Province")
        }

        binding.cvPickCity.setOnClickListener {
            newDialog("Select City")
        }
    }

    private fun newDialog(title: String, isProvinceNotNull: Boolean = false): CustomDialogFragment {
        val dialog = CustomDialogFragment()
        dialog.show(this.supportFragmentManager, "mDialog")
        arg.putString("title", title)
        //arg.putBoolean("isProvinceNotNull", isProvinceNotNull)
        dialog.arguments
        dialog.arguments = arg
        return dialog
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
            if(!isFieldsEmpty())binding.ilPatientName.error = getString(R.string.name_required)
        } else {
            isPatientNameValid = true
            createDonorReqViewModel.donorReq.patientName = name
        }
    }

    private fun checkNumberOfBloodBag() {
        val createDonorReqViewModel =
            ViewModelProvider(this)[CreateDonorReqViewModel::class.java]
        val number = binding.etNumberOfBloodNeeded.text.toString().trim()
        if (number.isEmpty()) {
            isNumberOfBloodBagValid = false
            if(!isFieldsEmpty())binding.ilNumberOfBloodNeeded.error = getString(R.string.name_required)
        } else if (number.toInt() > 5) {
            isNumberOfBloodBagValid = false
            if(!isFieldsEmpty())binding.ilNumberOfBloodNeeded.error = getString(R.string.max_blood_bag)
        } else {
            isNumberOfBloodBagValid = true
            createDonorReqViewModel.donorReq.phoneNumber = number
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
            createDonorReqViewModel.donorReq.bloodType = radio.text.toString().lowercase()
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
            createDonorReqViewModel.donorReq.rhesus = radio.text.toString().lowercase()
        }
    }

    private fun checkHospitalName() {
        val createDonorReqViewModel =
            ViewModelProvider(this)[CreateDonorReqViewModel::class.java]
        val name = binding.etHospitalName.text.toString().trim()
        if (name.isEmpty()) {
            isHospitalNameValid = false
            if(!isFieldsEmpty())binding.ilHospitalName.error = getString(R.string.hospital_name_required)
        } else {
            isHospitalNameValid = true
            createDonorReqViewModel.donorReq.hospitalName = name
        }
    }

    private fun checkDescription() {
        val createDonorReqViewModel =
            ViewModelProvider(this)[CreateDonorReqViewModel::class.java]
        val selectedPhoneNumber = binding.etDescription.text.toString().trim()
        if (selectedPhoneNumber.isEmpty()) {
            isDescriptionValid = false
            if(!isFieldsEmpty())binding.ilDescription.error = getString(R.string.des_required)
        } else if (selectedPhoneNumber.length > 200) {
            isDescriptionValid = false
            if(!isFieldsEmpty())binding.ilDescription.error = getString(R.string.des_max_length)
        } else
            isDescriptionValid = true
        createDonorReqViewModel.donorReq.description = selectedPhoneNumber

    }

    private fun checkContactName() {
        val createDonorReqViewModel =
            ViewModelProvider(this)[CreateDonorReqViewModel::class.java]
        val name = binding.etContactName.text.toString().trim()
        if (name.isEmpty()) {
            isContactNameValid = false
            if(!isFieldsEmpty())binding.ilContactName.error = getString(R.string.name_required)
        } else {
            isContactNameValid = true
            createDonorReqViewModel.donorReq.contactName = name
        }
    }

    private fun checkPhoneNumber() {
        val createDonorReqViewModel =
            ViewModelProvider(this)[CreateDonorReqViewModel::class.java]
        val selectedPhoneNumber = binding.etPhoneNumber.text.toString().trim()
        if (selectedPhoneNumber.isEmpty()) {
            isPhoneNumberValid = false
            if(!isFieldsEmpty())binding.ilPhone.error = getString(R.string.input_phone_number)
        } else if (selectedPhoneNumber.length < 9) {
            isPhoneNumberValid = false
            if(!isFieldsEmpty())binding.ilPhone.error = getString(R.string.phone_number_length_invalid)
        } else
            isPhoneNumberValid = true
        createDonorReqViewModel.donorReq.phoneNumber = selectedPhoneNumber
    }

    private fun clearAllError(){
        binding.ilPatientName.isErrorEnabled = false
        binding.ilPatientName.error = ""
        binding.ilNumberOfBloodNeeded.isErrorEnabled = false
        binding.ilNumberOfBloodNeeded.error = ""
        binding.ilHospitalName.isErrorEnabled = false
        binding.ilHospitalName.error = ""
        binding.ilDescription.isErrorEnabled = false
        binding.ilDescription.error = ""
        binding.ilContactName.isErrorEnabled = false
        binding.ilContactName.error = ""
        binding.ilPhone.isErrorEnabled = false
        binding.ilPhone.error = ""
    }

    override fun onFocusChange(v: View?, isFocused: Boolean) {
        if (v != null) {
            when (v.id) {
                R.id.et_patientName -> {
                    if (isFocused) {
                        binding.ilPatientName.isErrorEnabled = false
                        binding.ilPatientName.error = ""
                    } else {
                        checkPatientName()
                    }
                }
                R.id.et_number_of_blood_needed -> {
                    if (isFocused) {
                        binding.ilNumberOfBloodNeeded.isErrorEnabled = false
                        binding.ilNumberOfBloodNeeded.error = ""
                    } else {
                        checkNumberOfBloodBag()
                    }
                }
                R.id.et_hospital_name -> {
                    if (isFocused) {
                        binding.ilHospitalName.isErrorEnabled = false
                        binding.ilHospitalName.error = ""
                    } else {
                        checkHospitalName()
                    }
                }
                R.id.et_description -> {
                    if (isFocused) {
                        binding.ilDescription.isErrorEnabled = false
                        binding.ilDescription.error = ""
                    } else {
                        checkDescription()
                    }
                }
                R.id.et_contact_name -> {
                    if (isFocused) {
                        binding.ilContactName.isErrorEnabled = false
                        binding.ilContactName.error = ""
                    } else {
                        checkContactName()
                    }
                }
                R.id.et_phoneNumber -> {
                    if (isFocused) {
                        binding.ilPhone.isErrorEnabled = false
                        binding.ilPhone.error = ""
                    } else {
                        checkPhoneNumber()
                    }
                }
            }
        }
    }
}