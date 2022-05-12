package com.example.redminecapstoneproject.ui.loginsignup

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.FragmentLoginBinding
import com.example.redminecapstoneproject.ui.donordata.DonorDataActivity
import www.sanju.motiontoast.MotionToast

class LoginFragment : Fragment(), View.OnFocusChangeListener {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var isEmailValid: Boolean = false
    private var isPassValid: Boolean = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etEmail.onFocusChangeListener = this
        binding.etPassword.onFocusChangeListener = this

        binding.cbSeePassword.setOnClickListener {
            if (binding.cbSeePassword.isChecked) {
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        binding.btSignUp.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.btLogin.setOnClickListener {

            if (isDataValid()) {
                /*val user = RequestLogin(
                    binding.tiEmail.text.toString().trim(),
                    binding.tiPass.text.toString().trim()
                )*/
                MotionToast.Companion.createColorToast(
                    requireActivity(),
                    "Yey success 😍",
                    "Login successfully!",
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
                if (!isFieldsEmpty()) {
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "Hey careful",
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    private fun isFieldsEmpty(): Boolean {
        return binding.etEmail.text.toString().trim() == ""
                && binding.etPassword.text.toString().trim() == ""

    }

    private fun isDataValid(): Boolean {
        binding.etEmail.clearFocus()
        binding.etPassword.clearFocus()
        return isEmailValid && isPassValid
    }

    private fun checkEmail() {
        val email = binding.etEmail.text.toString().trim()
        if (email.isEmpty()) {
            isEmailValid = false
            binding.ilEmail.error = getString(R.string.input_email)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isEmailValid = false
            binding.ilEmail.error = getString(R.string.invalid_email)
        } else {
            isEmailValid = true
        }
    }

    private fun checkPass() {
        val pass = binding.etPassword.text.toString().trim()
        if (pass.isEmpty()) {
            isPassValid = false
            binding.ilPassword.error = getString(R.string.input_pass)
        } else if (pass.length < 6) {
            isPassValid = false
            binding.ilPassword.error = getString(R.string.invalid_password)
        } else {
            isPassValid = true
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
                        checkEmail()
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
            }
        }
    }

    companion object {
    }
}