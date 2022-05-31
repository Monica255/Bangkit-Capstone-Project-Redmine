package com.example.redminecapstoneproject.ui.profile

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.redminecapstoneproject.CustomDialogFragment
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.ActivityUserDetailBinding
import com.example.redminecapstoneproject.helper.helperUserDetail
import com.example.redminecapstoneproject.ui.testing.DonorDataRoom
import com.example.redminecapstoneproject.ui.testing.RegisAccountDataRoom
import com.example.redminecapstoneproject.ui.verifyaccount.VerifyAccountActivity
import www.sanju.motiontoast.MotionToast

class UserDetailActivity : AppCompatActivity(), View.OnFocusChangeListener {
    private lateinit var binding: ActivityUserDetailBinding
    var arg = Bundle()

    private var isEmailValid = false
        get() {
            checkEmail()
            return field
        }
    private var isNameValid = false
        get() {
            checkName()
            return field
        }
    private var isPhoneNumberValid = false
        get() {
            checkPhoneNumber()
            return field
        }
    private var isCityValid = false
        get() {
            checkCity()
            return field
        }

    private var province = ""
        set(value) {
            field = value
            binding.tvProvince.text = value
            binding.tvProvince.alpha = 1F
        }

    private var city = ""
        set(value) {
            field = value
            binding.tvCity.text = value
            binding.tvCity.alpha = 1F
        }

    private lateinit var accountData: RegisAccountDataRoom
    private var isVerified:Boolean?=null
    private var isVerified2:Boolean?=null

    private val resultCOntract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
            if (result?.resultCode == Activity.RESULT_OK) {


            }
        }

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
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userDetailViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[UserDetailViewModel::class.java]
        binding.etName.onFocusChangeListener = this
        binding.etEmail.onFocusChangeListener = this
        binding.etPhoneNumber.onFocusChangeListener = this


        userDetailViewModel.userData.observe(this) {
            if(isVerified!=it.isVerified && isVerified!=null){
                userDetailViewModel._newUserData.value = newUserData(it)

            }
            isVerified=it.isVerified

            if (userDetailViewModel._newUserData.value == null) {
                userDetailViewModel._newUserData.value = newUserData(it)
                userDetailViewModel.getCities(
                    helperUserDetail.getProvinceID(it.province.toString())
                )            }
            Log.d("TAG", userDetailViewModel._newUserData.value.toString())
            if (userDetailViewModel._newUserData.value != null) setData(it)

        }

        userDetailViewModel.accountData.observe(this) {
            if(isVerified2!=it.isVerified && isVerified2!=null){
                userDetailViewModel._newAccountData.value = newAccountData(it)

            }
            isVerified2=it.isVerified
            if (userDetailViewModel._newAccountData.value == null) {
                userDetailViewModel._newAccountData.value = newAccountData(it)
                //userDetailViewModel.tempAccountData = newAccountData(it)
            }
            Log.d("TAG", userDetailViewModel._newAccountData.value.toString())
            if (userDetailViewModel._newAccountData.value != null) setData(it)
        }


        userDetailViewModel._newUserData.observe(this) {
            setButtonSaveEnable(userDetailViewModel.isDataDifferent())
        }

        userDetailViewModel._newAccountData.observe(this) {
            setButtonSaveEnable(userDetailViewModel.isDataDifferent())
        }

        userDetailViewModel.message.observe(this) {
            makeToast(it.first, it.second)
        }




        binding.etName.addTextChangedListener {
            userDetailViewModel.updateAccountDataRoom(Pair(binding.etName.text.toString(), "name"))
        }

        binding.etEmail.addTextChangedListener {
            userDetailViewModel.updateAccountDataRoom(
                Pair(
                    binding.etEmail.text.toString(),
                    "email"
                )
            )
        }

        binding.etPhoneNumber.addTextChangedListener {
            userDetailViewModel.updateDonorDataRoom(
                Pair(
                    binding.etPhoneNumber.text.toString(),
                    "number"
                )
            )
        }

        binding.cvPickProvince.setOnClickListener {
            newDialog("Select Province")
        }

        binding.cvPickCity.setOnClickListener {
            newDialog("Select City")
        }

        binding.btSave.setOnClickListener {
            showConfirmDialog("save", userDetailViewModel)
            Log.d("TAG", "saved")
        }

        binding.btDiscard.setOnClickListener {
            showConfirmDialog("discard", userDetailViewModel)
            Log.d("TAG", "discarded")
        }

        binding.btBack.setOnClickListener {
            onBackPressed()
        }

        binding.btVerifyAccount.setOnClickListener {
            val userDetailViewModel = ViewModelProvider(
                this,
                RepoViewModelFactory.getInstance(this)
            )[UserDetailViewModel::class.java]
            if (userDetailViewModel.isDataDifferent()) {
                showConfirmDialog("verify", userDetailViewModel)
            } else {
                verifyAccount(userDetailViewModel.userData.value?.uid,userDetailViewModel.userData.value?.isVerified)
            }

        }


    }

    private fun verifyAccount(uid:String?=null,verified:Boolean?=null){
        if(uid!=null && verified!= null && verified==false){
            var intent=Intent(this,VerifyAccountActivity::class.java)
            intent.putExtra(VerifyAccountActivity.UID,uid)
            resultCOntract.launch(intent)
        }else{
            //make toast
        }

    }

    override fun onBackPressed() {
        val userDetailViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[UserDetailViewModel::class.java]
        if (userDetailViewModel.isDataDifferent()) {
            showConfirmDialog("back", userDetailViewModel)
        } else super.onBackPressed()
    }


    private fun setButtonSaveEnable(x: Boolean) {
        if (x && isDataValid()) {
            binding.btSave.isEnabled = true
            binding.btSave.alpha = 1F
            binding.btDiscard.visibility = View.VISIBLE
            Log.d("TAG", "data dif n valid")
        } else if (x) {
            binding.btSave.isEnabled = false
            binding.btSave.alpha = 0.5F
            binding.btDiscard.visibility = View.VISIBLE
            Log.d("TAG", "data dif n invalid")
        } else {
            binding.btSave.isEnabled = false
            binding.btSave.alpha = 0.5F
            binding.btDiscard.visibility = View.GONE
            Log.d("TAG", "data same/ invalid")
        }
    }

    private fun setData(user: Any) {
        val states = arrayOf(
            intArrayOf(android.R.attr.state_enabled),
        )

        val colors = intArrayOf(
            R.color.green
        )
        if (user is DonorDataRoom) {
            binding.apply {
                ilPhone.editText?.setText(user.phoneNumber)
                province = helperUserDetail.toTitleCase(user.province?.let { helperUserDetail.getProvinceName(it)}) ?: "Province"
                city = helperUserDetail.toTitleCase(user.city )?: "City"
                user.gender?.let { setAvatar(it) }

                if (user.isVerified == true) {
                    layoutVerifiedAccount.visibility = View.VISIBLE
                    layoutUnverifiedAccount.visibility = View.GONE
                    layoutVerifyAccount.visibility = View.GONE
                    etName.isEnabled = false
                    ilName.helperText = "You cant edit this field"
                    ilName.editText?.setTextColor(resources.getColor(R.color.not_so_black, theme))
                    ilName.setHelperTextColor(ColorStateList(states, colors))
                } else {
                    layoutVerifiedAccount.visibility = View.GONE
                    layoutUnverifiedAccount.visibility = View.VISIBLE
                    layoutVerifyAccount.visibility = View.VISIBLE
                    etName.isEnabled = true
                    ilName.helperText = ""
                }
            }
        } else if (user is RegisAccountDataRoom) {

            binding.apply {
                ilName.editText?.setText(user.name)
                ilEmail.editText?.setText(user.email)
                ilEmail.helperText = "You cant edit this field"
                ilEmail.editText?.setTextColor(resources.getColor(R.color.not_so_black, theme))
                ilEmail.setHelperTextColor(ColorStateList(states, colors))


            }
        }

    }

    private fun setAvatar(data: String) {
        val x: Int =
            if (data == "male") R.drawable.img_profile_placeholder_male else R.drawable.img_profile_placeholder_female
        Glide.with(this)
            .load(x)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .fallback(R.drawable.ic_launcher_foreground)
            .into(binding.imgProfile)
    }

    private fun newUserData(x: DonorDataRoom): DonorDataRoom {
        return DonorDataRoom(
            x.uid,
            x.isVerified,
            x.gender,
            x.bloodType,
            x.rhesus,
            x.phoneNumber,
            x.province,
            x.city,
            x.haveDonated,
            x.hadCovid,
            x.lastDonateDate,
            x.recoveryDate
        )
    }

    private fun newAccountData(x: RegisAccountDataRoom): RegisAccountDataRoom {
        return RegisAccountDataRoom(
            x.uid,
            x.isVerified,
            x.name,
            x.email,
            x.otpCode
        )
    }

    private fun isDataValid(): Boolean {
        return isNameValid && isEmailValid && isPhoneNumberValid && isCityValid
    }

    private fun checkName() {
        val name = binding.etName.text.toString().trim()
        isNameValid = name.isNotEmpty()
    }

    private fun setNameErrorText() {
        checkName()
        if (!isNameValid) binding.ilName.error = getString(R.string.name_required)
    }

    private fun checkEmail() {
        val email = binding.etEmail.text.toString().trim()
        isEmailValid = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun setEmailErrorText() {
        checkEmail()
        val email = binding.etEmail.text.toString().trim()
        if (email.isEmpty()) {
            binding.ilEmail.error = getString(R.string.email_required)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.ilEmail.error = getString(R.string.invalid_email)
        }
    }

    private fun checkPhoneNumber() {
        val phoneNumber = binding.etPhoneNumber.text.toString().trim()
        isPhoneNumberValid = phoneNumber.isNotEmpty() && phoneNumber.length >= 9
    }

    private fun checkCity() {
        val userDetailViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[UserDetailViewModel::class.java]
        val city = userDetailViewModel._newUserData.value?.city
        isCityValid = city != null
    }

    private fun setPhoneErrorText() {
        checkPhoneNumber()
        val phoneNumber = binding.etPhoneNumber.text.toString().trim()
        if (phoneNumber.isEmpty()) {
            binding.ilPhone.error = getString(R.string.input_phone_number)
        } else if (phoneNumber.length < 9) {
            binding.ilPhone.error = getString(R.string.phone_number_length_invalid)
        }
    }

    private fun showConfirmDialog(x: String, vm: UserDetailViewModel) {
        val builder = AlertDialog.Builder(this)
        val mConfirmDialog = builder.create()
        if (x == "save") {
            builder.setTitle(getString(R.string.save))
            builder.setMessage("Are you sure you want to save any changes?")
        } else if (x == "discard") {

            builder.setTitle(getString(R.string.discard_changes))
            builder.setMessage("Are you sure you want to discard any changes?")
        } else if (x == "back") {
            builder.setTitle(getString(R.string.unsaved_changes))
            builder.setMessage("Are you sure you want to leave without saving?")
        } else if (x == "verify") {
            builder.setTitle(getString(R.string.unsaved_changes))
            builder.setMessage("Are you sure you want to leave without saving?")
        }


        builder.create()


        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            if (x == "discard") {
                resetTemptData(vm)
                binding.btDiscard.visibility = View.GONE
                setButtonSaveEnable(false)
            } else if (x == "back") {
                resetTemptData(vm)
                setButtonSaveEnable(false)
                super.onBackPressed()
            } else if (x == "verify") {
                resetTemptData(vm)
                setButtonSaveEnable(false)
                verifyAccount()
            } else {
                val userDetailViewModel = ViewModelProvider(
                    this,
                    RepoViewModelFactory.getInstance(this)
                )[UserDetailViewModel::class.java]

                userDetailViewModel._newUserData.value?.let {
                    userDetailViewModel.saveUserDonorData(
                        it
                    )
                }
                userDetailViewModel._newAccountData.value?.let {
                    userDetailViewModel.saveUserAccountData(
                        it
                    )
                }
                setButtonSaveEnable(false)
                //save data

            }

        }

        builder.setNegativeButton(getString(R.string.no)) { _, _ ->
            mConfirmDialog.cancel()
        }

        builder.show()
    }

    private fun resetTemptData(vm: UserDetailViewModel) {
        vm.searchCityQuery=null
        vm.searchProvinceQuery=null
        vm.userData.value?.let {
            vm._newUserData.value = newUserData(it)
            Log.d("TAG", "renew ud " + vm._newUserData.value)
            setData(it)
        }
        vm.accountData.value?.let {
            vm._newAccountData.value = newAccountData(it)
            setData(it)
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


    override fun onFocusChange(v: View?, isFocused: Boolean) {
        if (v != null) {
            when (v.id) {
                R.id.et_email -> {
                    if (isFocused) {
                        binding.ilEmail.isErrorEnabled = false
                        binding.ilEmail.error = ""
                    } else {
                        setEmailErrorText()
                    }
                }
                R.id.et_name -> {
                    if (isFocused) {
                        binding.ilName.isErrorEnabled = false
                        binding.ilName.error = ""
                    } else {
                        setNameErrorText()
                    }
                }
                R.id.et_phoneNumber -> {
                    if (isFocused) {
                        binding.ilPhone.isErrorEnabled = false
                        binding.ilPhone.error = ""
                    } else {
                        setPhoneErrorText()
                    }
                }

            }
        }


    }

    companion object{
        const val NEW_NAME="new_name"
        const val NEW_GENDER="new_gender"
    }
}