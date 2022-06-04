package com.example.redminecapstoneproject.ui.loginsignup

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.redminecapstoneproject.LoadingUtils
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.FragmentSignupBinding
import com.example.redminecapstoneproject.ui.donordata.DonorDataActivity
import com.example.redminecapstoneproject.ui.otp.OtpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import www.sanju.motiontoast.MotionToast


class SignupFragment : Fragment(), View.OnFocusChangeListener {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private var counter2=0

    /*private val loginSignupViewModel: LoginSignupViewModel by viewModels {
        RepoViewModelFactory(requireActivity())
    }*/


    private var mAuth: FirebaseAuth? = null
    private var mDb: FirebaseDatabase? = null
    private var isEmailValid: Boolean = false
        get() {
            checkEmail()
            return field
        }

    private var isNameValid: Boolean = false
        get() {
            checkName()
            return field
        }

    private var isPassValid: Boolean = false
        get() {
            checkPass()
            return field
        }

    private var iscPassValid: Boolean = false
        get() {
            checkConfirmPass()
            return field
        }

    lateinit var name: String
    private lateinit var email: String
    private lateinit var pass: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        mDb = FirebaseDatabase.getInstance()
        binding.etName.onFocusChangeListener = this
        binding.etEmail.onFocusChangeListener = this
        binding.etPassword.onFocusChangeListener = this
        binding.etCpassword.onFocusChangeListener = this




        clearAllFocus()
        val loginSignupViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[LoginSignupViewModel::class.java]

        loginSignupViewModel.message.observe(requireActivity()) {
            makeToast(it.first, it.second)
        }

        //val intent = Intent(activity, LoginActivity::class.java)
        val intent2 = Intent(activity, DonorDataActivity::class.java)
        //val intent3 = Intent(activity, HomeActivity::class.java)
        val intent4 = Intent(activity, OtpActivity::class.java)

        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        //intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

        if(isAdded){
            loginSignupViewModel.firebaseUser.observe(requireActivity()) { fu ->
                if (fu == null) {
                    //startActivity(intent)
                } else {

                    if(isAdded){
                        loginSignupViewModel.getUserAccountDataDb()
                            .observe(requireActivity()) { value ->

                                if (value == null) {
                                    /*Log.d("TAG", "first acc null data")
                                    counter2++
                                    if (counter2 > 1) {
                                    Log.d("TAG", "acc null")
                                    loginSignupViewModel.setUserAccountData()
                                    }*/
                                } else if (value.otpCode == null) {
                                    intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent4)
                                    activity?.finish()
                                } else {
                                    startActivity(intent2)
                                    activity?.finish()
                                }
                            }
                    }


                }
            }
        }

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
                loginSignupViewModel.registerAccount(email, pass, name)
            } else {
                if (!isFieldsEmpty()) {
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        getString(R.string.hey_careful),
                        getString(R.string.please_enter_data_correctly),
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

        loginSignupViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

    }

    private fun showLoading(show: Boolean) {
        if (show) {
            if (isAdded) {
                LoadingUtils.showDialog(context, false)
            }
        } else {
            LoadingUtils.hideDialog()
        }
    }

    private fun makeToast(isError: Boolean, msg: String) {
        if(isAdded){
            if (isError) {
                MotionToast.Companion.createColorToast(
                    requireActivity(),
                    "Ups",
                    msg,
                    MotionToast.TOAST_ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(
                        requireActivity(),
                        www.sanju.motiontoast.R.font.helvetica_regular
                    )
                )
            } else {
                MotionToast.Companion.createColorToast(
                    requireActivity(),
                    getString(R.string.yey_success),
                    msg,
                    MotionToast.TOAST_SUCCESS,
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


    private fun clearAllFocus() {
        binding.apply {
            etName.clearFocus()
            etEmail.clearFocus()
            etPassword.clearFocus()
            etCpassword.clearFocus()

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun isFieldsEmpty(): Boolean {
        return binding.etEmail.text.toString().trim() == ""
                && binding.etPassword.text.toString().trim() == ""
                && binding.etName.text.toString().trim() == ""
                && binding.etCpassword.text.toString().trim() == ""

    }

    private fun isDataValid(): Boolean {
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
        } else {
            isPassValid = true
            iscPassValid = true

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

    companion object;

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