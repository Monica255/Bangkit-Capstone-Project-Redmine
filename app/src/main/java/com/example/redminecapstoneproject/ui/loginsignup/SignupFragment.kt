package com.example.redminecapstoneproject.ui.loginsignup

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.FragmentCanDonateBloodBinding
import com.example.redminecapstoneproject.databinding.FragmentSignupBinding
import com.example.redminecapstoneproject.ui.donordata.DonorDataActivity
import www.sanju.motiontoast.MotionToast

class SignupFragment : Fragment(), View.OnFocusChangeListener {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private var isEmailValid: Boolean = false
    private var isNameValid: Boolean = false
    private var isPassValid: Boolean = false
    private var iscPassValid: Boolean = false
    lateinit var name: String
    private lateinit var email: String
    private lateinit var pass: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etName.onFocusChangeListener = this
        binding.etEmail.onFocusChangeListener = this
        binding.etPassword.onFocusChangeListener = this
        binding.etCpassword.onFocusChangeListener = this

        binding.cbSeePassword.setOnClickListener {
            if (binding.cbSeePassword.isChecked) {
                binding.etPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.etCpassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()

            } else {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.etCpassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }

        binding.btBack.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        binding.btSignup.setOnClickListener {


            if (isDataValid()) {
                name = binding.etName.text.toString().trim()
                email = binding.etEmail.text.toString().trim()
                pass = binding.etPassword.text.toString().trim()

                MotionToast.Companion.createColorToast(
                    requireActivity(),
                    "Yey success 😍",
                    "Your account is successfully registered!",
                    MotionToast.TOAST_SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(
                        requireActivity(),
                        www.sanju.motiontoast.R.font.helvetica_regular
                    )
                )

                val intent = Intent(activity, DonorDataActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                if(!isFieldsEmpty()){
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "Hey careful ",
                        "Please enter you data correctly",
                        MotionToast.TOAST_WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(
                            requireActivity(),
                            www.sanju.motiontoast.R.font.helvetica_regular
                        )
                    )
                }

            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    private fun isFieldsEmpty(): Boolean {
        return binding.etEmail.text.toString().trim()==""
                && binding.etPassword.text.toString().trim()==""
                && binding.etName.text.toString().trim()==""
                && binding.etCpassword.text.toString().trim()==""

    }

    private fun isDataValid(): Boolean {
        binding.apply {
            etEmail.clearFocus()
            etName.clearFocus()
            etPassword.clearFocus()
            etCpassword.clearFocus()
        }
        return isNameValid && isEmailValid && isPassValid && iscPassValid
    }

    private fun checkEmail() {

        val email = binding.etEmail.text.toString().trim()
        if (email.isEmpty()) {
            isEmailValid = false
            binding.ilEmail.error = getString(R.string.email_required)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isEmailValid = false
            binding.ilEmail.error = getString(R.string.invalid_email)
        } else {
            isEmailValid = true
        }
    }

    private fun checkName() {
        val name = binding.etName.text.toString().trim()
        if (name.isEmpty()) {
            isNameValid = false
            binding.ilName.error = getString(R.string.name_required)
        } else {
            isNameValid = true
        }
    }

    private fun checkPass() {
        val cpass = binding.etCpassword.text.toString().trim()
        val pass = binding.etPassword.text.toString().trim()
        if (pass.isEmpty()) {
            isPassValid = false
            binding.ilPassword.error = getString(R.string.password_required)
        } else if (pass.length < 6) {
            isPassValid = false
            binding.ilPassword.error = getString(R.string.pass_length)
        } else if (cpass != pass) {
                iscPassValid = false
                binding.ilCpassword.error = getString(R.string.pass_not_match)
        }else {
            isPassValid = true
            binding.ilCpassword.isErrorEnabled = false
            binding.ilCpassword.error = ""
        }
    }

    private fun checkConfirmPass() {
        val cpass = binding.etCpassword.text.toString().trim()
        val pass = binding.etPassword.text.toString().trim()

        if (cpass.isEmpty()) {
            iscPassValid = false
            binding.ilCpassword.error = getString(R.string.cpass_required)
        } else if (cpass != pass) {
            iscPassValid = false
            binding.ilCpassword.error = getString(R.string.pass_not_match)

        } else {
            iscPassValid = true

        }
    }

    companion object {

    }

    override fun onFocusChange(v: View?, isFocused: Boolean) {
        if (v != null) {
            when (v.id) {
                R.id.et_email -> {
                    if (isFocused) {
                        Log.d("TAG", "is email focused")
                        binding.ilEmail.isErrorEnabled = false
                        binding.ilEmail.error = ""
                    } else {
                        checkEmail()
                    }
                }
                R.id.et_name -> {
                    if (isFocused) {
                        binding.ilName.isErrorEnabled = false
                        binding.ilName.error = ""
                    } else {
                        checkName()
                    }
                }
                R.id.et_password -> {
                    if (isFocused) {
                        binding.ilPassword.isErrorEnabled = false
                        binding.ilPassword.error = ""
                    } else {
                        checkPass()
                    }
                }
                R.id.et_cpassword -> {
                    if (isFocused) {
                        binding.ilCpassword.isErrorEnabled = false
                        binding.ilCpassword.error = ""
                    } else {
                        checkConfirmPass()
                    }
                }
            }
        }
    }
}