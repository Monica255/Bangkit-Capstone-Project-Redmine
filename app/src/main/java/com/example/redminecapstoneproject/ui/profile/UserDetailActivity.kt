package com.example.redminecapstoneproject.ui.profile

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.CustomDialogFragment
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ActivityUserDetailBinding
import com.example.redminecapstoneproject.ui.testing.UserDetail

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

    private lateinit var userDetail: UserDetail

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
        val userDetailViewModel =
            ViewModelProvider(this)[UserDetailViewModel::class.java]

        binding.etName.onFocusChangeListener = this
        binding.etEmail.onFocusChangeListener = this
        binding.etPhoneNumber.onFocusChangeListener = this

        userDetailViewModel.userDetail.observe(this) {
            userDetail = it
            setData(it)
            if (userDetailViewModel._newUserDetail.value == null) {
                userDetailViewModel._newUserDetail.value = newUserData(it)
            }
        }


        userDetailViewModel._newUserDetail.observe(this) {
            setButtonSaveEnable(userDetailViewModel.isDataDifferent())
        }

        binding.etName.addTextChangedListener {
            userDetailViewModel.updateUserDetail(binding.etName.text.toString(), "name")
        }

        binding.etEmail.addTextChangedListener {
            userDetailViewModel.updateUserDetail(binding.etEmail.text.toString(), "email")
        }

        binding.etPhoneNumber.addTextChangedListener {
            userDetailViewModel.updateUserDetail(binding.etPhoneNumber.text.toString(), "number")
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
            finish()
        }


    }

    override fun onDestroy() {
        Log.d("TAG", "destroy")
        super.onDestroy()
    }

    private fun setButtonSaveEnable(x: Boolean) {
        if (x && isDataValid()) {
            binding.btSave.isEnabled = true
            binding.btSave.alpha = 1F
            binding.btDiscard.visibility = View.VISIBLE
            Log.d("TAG", "data dif n valid")
        } else {
            binding.btSave.isEnabled = false
            binding.btSave.alpha = 0.5F
            binding.btDiscard.visibility = View.GONE
            Log.d("TAG", "data same/ not valid")
        }
    }

    private fun setData(user: UserDetail) {
        binding.apply {
            ilName.editText?.setText(user.name)
            ilEmail.editText?.setText(user.email)
            ilPhone.editText?.setText(user.phoneNumber)
            province = user.province
            city = user.city
        }
    }

    private fun newUserData(x: UserDetail): UserDetail {
        return UserDetail(
            x.isVerified, x.name, x.email, x.phoneNumber, x.province, x.city
        )
    }

    private fun isDataValid(): Boolean {
        return isNameValid && isEmailValid && isPhoneNumberValid
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
        }


        builder.create()

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            if (x == "discard") {
                vm._newUserDetail.value = newUserData(userDetail)

                setData(userDetail)
                binding.btDiscard.visibility = View.GONE
                setButtonSaveEnable(false)
            } else {
                //save data
            }

        }

        builder.setNegativeButton(getString(R.string.no)) { _, _ ->
            mConfirmDialog.cancel()
        }

        builder.show()
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
}