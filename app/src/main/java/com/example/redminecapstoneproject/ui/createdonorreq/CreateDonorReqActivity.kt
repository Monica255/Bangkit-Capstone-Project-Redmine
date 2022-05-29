package com.example.redminecapstoneproject.ui.createdonorreq

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.CustomDialogFragment
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.ActivityCreateDonorReqBinding
import com.example.redminecapstoneproject.helper.helperBloodDonors
import com.example.redminecapstoneproject.helper.helperDate
import com.example.redminecapstoneproject.helper.helperUserDetail
import com.example.redminecapstoneproject.ui.mydonorreq.MyDonorReqActivity
import com.example.redminecapstoneproject.ui.profile.UserDetailViewModel
import com.example.redminecapstoneproject.ui.testing.DonorDataRoom
import com.example.redminecapstoneproject.ui.testing.DonorRequest
import com.example.redminecapstoneproject.ui.testing.RegisAccountDataRoom
import com.google.firebase.auth.FirebaseAuth
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
        get() {
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

        val createDonorReqViewModel =
            ViewModelProvider(
                this,
                RepoViewModelFactory.getInstance(this)
            )[CreateDonorReqViewModel::class.java]


        val donorReq= intent.getParcelableExtra<DonorRequest>(EXTRA_DONOR_REQ)

        if(donorReq!=null){
            setData(donorReq)
        }else{
            createDonorReqViewModel.accountData.observe(this){
                setInitialData(it)
            }

            createDonorReqViewModel.donorData.observe(this){
                setInitialData(it)
            }
        }


        createDonorReqViewModel.message.observe(this){
            makeToast(it.first,it.second)
        }

        binding.btBack.setOnClickListener {
            finish()
        }

        binding.btPost.setOnClickListener {
            if (isDataValid()) {
                createDonorReqViewModel.donorReq.uid=FirebaseAuth.getInstance().currentUser?.uid
                createDonorReqViewModel.donorReq.time=helperDate.getCurrentTime()
                createDonorReqViewModel.donorReq.timestamp="-${ System.currentTimeMillis() }".toLong()

                FirebaseAuth.getInstance().currentUser?.uid?.let { it1 ->
                    createDonorReqViewModel.postDonorReq(createDonorReqViewModel.donorReq,
                        it1
                    )
                }
                Log.d("DONOR",createDonorReqViewModel.donorReq.toString())

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

        binding.btDelete.setOnClickListener {
            if(donorReq?.uid!=null&&donorReq.timestamp!=null){
                showConfirmDialog(donorReq.uid.toString(),donorReq.timestamp.toString())
            }
        }

    }


    private fun showConfirmDialog(uid:String,timeStamp:String) {
        val createDonorReqViewModel =
            ViewModelProvider(
                this,
                RepoViewModelFactory.getInstance(this)
            )[CreateDonorReqViewModel::class.java]
        var donorReqId=helperBloodDonors.toDonorReqId(uid,timeStamp)
        val builder = AlertDialog.Builder(this)
        val mConfirmDialog = builder.create()


        builder.create()
        builder.setTitle(getString(R.string.delete))
        builder.setMessage("Are you sure you want to delete this donor request?")

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            createDonorReqViewModel.deleteDonorReq(donorReqId)
            mConfirmDialog.cancel()
        }

        builder.setNegativeButton(getString(R.string.no)) { _, _ ->
            mConfirmDialog.cancel()
        }

        builder.show()
    }



    private fun setData(data:DonorRequest){
        val createDonorReqViewModel =
            ViewModelProvider(
                this,
                RepoViewModelFactory.getInstance(this)
            )[CreateDonorReqViewModel::class.java]
        createDonorReqViewModel.donorReq=data
        binding.apply {
            tvToolbarTitle.text="Donor Request"
            btDelete.visibility=View.VISIBLE
            etPatientName.setText(data.patientName)
            etPatientName.isEnabled=false
            etPatientName.setTextColor(resources.getColor(R.color.not_so_black,theme))

            data.numberOfBloodBag?.let { etNumberOfBloodNeeded.setText(it.toString()) }
            etNumberOfBloodNeeded.isEnabled=false
            etNumberOfBloodNeeded.setTextColor(resources.getColor(R.color.not_so_black,theme))

            rgBloodType.check(
                when (data.bloodType) {
                    "a" -> R.id.rb_a
                    "b" -> R.id.rb_b
                    "ab" -> R.id.rb_ab
                    "o" -> R.id.rb_o
                    else -> -1
                }
            )

            rbA.isClickable=false
            rbB.isClickable=false
            rbAb.isClickable=false
            rbO.isClickable=false


            rgRhesus.check(if (data.rhesus == "positive") R.id.rb_positive else if (data.rhesus == "negative") R.id.rb_negative else R.id.rb_dont_know)
            rbPositive.isClickable=false
            rbNegative.isClickable=false

            tvProvince.text=helperUserDetail.getProvinceName(data.province.toString()).lowercase()?.replaceFirstChar(Char::titlecase)
            cvPickProvince.isEnabled=false
            tvProvince.alpha=1F

            tvCity.text=data.city?.lowercase()?.replaceFirstChar(Char::titlecase)
            cvPickCity.isEnabled=false
            tvCity.alpha=1F

            etHospitalName.setText(data.hospitalName)
            etHospitalName.isEnabled=false
            etHospitalName.setTextColor(resources.getColor(R.color.not_so_black,theme))

            etDescription.setText(data.description)
            etDescription.isEnabled=false
            etDescription.setTextColor(resources.getColor(R.color.not_so_black,theme))

            etContactName.setText(data.contactName)
            etContactName.isEnabled=false
            etContactName.setTextColor(resources.getColor(R.color.not_so_black,theme))

            etPhoneNumber.setText(data.phoneNumber)
            etPhoneNumber.isEnabled=false

            btPost.visibility=View.GONE

        }
    }

    private fun setInitialData(data:Any?){
        val createDonorReqViewModel =
            ViewModelProvider(
                this,
                RepoViewModelFactory.getInstance(this)
            )[CreateDonorReqViewModel::class.java]
        if(data is RegisAccountDataRoom){
            binding.etContactName.setText(data.name)
            createDonorReqViewModel.donorReq.contactName=data.name
        }else if(data is DonorDataRoom){
            binding.etPhoneNumber.setText(data.phoneNumber)
            createDonorReqViewModel.donorReq.phoneNumber=data.phoneNumber

            binding.tvProvince.text=helperUserDetail.getProvinceName(data.province.toString()).lowercase()?.replaceFirstChar(Char::titlecase)
            createDonorReqViewModel.donorReq.province=data.province
            binding.tvProvince.alpha=1F

            binding.tvCity.text=data.city?.lowercase()?.replaceFirstChar(Char::titlecase)
            createDonorReqViewModel.donorReq.city=data.city
            binding.tvCity.alpha=1F

        }
    }



    private fun makeToast(isError: Boolean, msg: String) {
        if (isError) {
            MotionToast.Companion.createColorToast(
                this,
                "Ups",
                msg,
                MotionToast.TOAST_ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(
                    this,
                    www.sanju.motiontoast.R.font.helvetica_regular
                )
            )
        } else {
            if(msg.contains("delete",true)){
                MotionToast.Companion.createColorToast(
                    this,
                    "Deleted",
                    msg,
                    MotionToast.TOAST_INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(
                        this,
                        www.sanju.motiontoast.R.font.helvetica_regular
                    )
                )
                finish()
            }else{
                MotionToast.Companion.createColorToast(
                    this,
                    "Yey success 😍",
                    msg,
                    MotionToast.TOAST_SUCCESS,
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


    private fun newDialog(title: String): CustomDialogFragment {
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
            ViewModelProvider(
                this,
                RepoViewModelFactory.getInstance(this)
            )[CreateDonorReqViewModel::class.java]
        val name = binding.etPatientName.text.toString().trim()
        if (name.isEmpty()) {
            isPatientNameValid = false
            if (!isFieldsEmpty()) binding.ilPatientName.error = getString(R.string.name_required)
        } else {
            isPatientNameValid = true
            createDonorReqViewModel.donorReq.patientName = name
        }
    }

    private fun checkNumberOfBloodBag() {
        val createDonorReqViewModel =
            ViewModelProvider(
                this,
                RepoViewModelFactory.getInstance(this)
            )[CreateDonorReqViewModel::class.java]
        val number = binding.etNumberOfBloodNeeded.text.toString().trim()
        if (number.isEmpty()) {
            isNumberOfBloodBagValid = false
            if (!isFieldsEmpty()) binding.ilNumberOfBloodNeeded.error =
                getString(R.string.name_required)
        } else if (number.toInt() > 5) {
            isNumberOfBloodBagValid = false
            if (!isFieldsEmpty()) binding.ilNumberOfBloodNeeded.error =
                getString(R.string.max_blood_bag)
        } else {
            isNumberOfBloodBagValid = true
            createDonorReqViewModel.donorReq.numberOfBloodBag = number.toInt()
        }
    }

    private fun checkBloodType() {
        val createDonorReqViewModel =
            ViewModelProvider(
                this,
                RepoViewModelFactory.getInstance(this)
            )[CreateDonorReqViewModel::class.java]
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
            ViewModelProvider(
                this,
                RepoViewModelFactory.getInstance(this)
            )[CreateDonorReqViewModel::class.java]
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
            ViewModelProvider(
                this,
                RepoViewModelFactory.getInstance(this)
            )[CreateDonorReqViewModel::class.java]
        val name = binding.etHospitalName.text.toString().trim()
        if (name.isEmpty()) {
            isHospitalNameValid = false
            if (!isFieldsEmpty()) binding.ilHospitalName.error =
                getString(R.string.hospital_name_required)
        } else {
            isHospitalNameValid = true
            createDonorReqViewModel.donorReq.hospitalName = name
        }
    }

    private fun checkDescription() {
        val createDonorReqViewModel =
            ViewModelProvider(
                this,
                RepoViewModelFactory.getInstance(this)
            )[CreateDonorReqViewModel::class.java]
        val selectedPhoneNumber = binding.etDescription.text.toString().trim()
        if (selectedPhoneNumber.isEmpty()) {
            isDescriptionValid = false
            if (!isFieldsEmpty()) binding.ilDescription.error = getString(R.string.des_required)
        } else if (selectedPhoneNumber.length > 200) {
            isDescriptionValid = false
            if (!isFieldsEmpty()) binding.ilDescription.error = getString(R.string.des_max_length)
        } else
            isDescriptionValid = true
        createDonorReqViewModel.donorReq.description = selectedPhoneNumber

    }

    private fun checkContactName() {
        val createDonorReqViewModel =
            ViewModelProvider(
                this,
                RepoViewModelFactory.getInstance(this)
            )[CreateDonorReqViewModel::class.java]
        val name = binding.etContactName.text.toString().trim()
        if (name.isEmpty()) {
            isContactNameValid = false
            if (!isFieldsEmpty()) binding.ilContactName.error = getString(R.string.name_required)
        } else {
            isContactNameValid = true
            createDonorReqViewModel.donorReq.contactName = name
        }
    }

    private fun checkPhoneNumber() {
        val createDonorReqViewModel =
            ViewModelProvider(
                this,
                RepoViewModelFactory.getInstance(this)
            )[CreateDonorReqViewModel::class.java]
        val selectedPhoneNumber = binding.etPhoneNumber.text.toString().trim()
        if (selectedPhoneNumber.isEmpty()) {
            isPhoneNumberValid = false
            if (!isFieldsEmpty()) binding.ilPhone.error = getString(R.string.input_phone_number)
        } else if (selectedPhoneNumber.length < 9) {
            isPhoneNumberValid = false
            if (!isFieldsEmpty()) binding.ilPhone.error =
                getString(R.string.phone_number_length_invalid)
        } else
            isPhoneNumberValid = true
        createDonorReqViewModel.donorReq.phoneNumber = selectedPhoneNumber
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

    companion object{
        const val EXTRA_DONOR_REQ="extra_donor_req"
    }

}