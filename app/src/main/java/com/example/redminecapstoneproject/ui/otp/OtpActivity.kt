package com.example.redminecapstoneproject.ui.otp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.ActivityOtpBinding
import com.example.redminecapstoneproject.ui.HomeActivity
import com.example.redminecapstoneproject.ui.donordata.DonorDataActivity
import com.example.redminecapstoneproject.ui.loginsignup.LoginActivity
import com.example.redminecapstoneproject.ui.loginsignup.LoginSignupViewModel
import com.example.redminecapstoneproject.ui.profile.UserDetailViewModel
import com.example.redminecapstoneproject.ui.testing.RegisAccountDataRoom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import www.sanju.motiontoast.MotionToast
import java.util.*

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private var otpCode = ""
    private lateinit var mAccountData: RegisAccountDataRoom
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOtpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListener()

        initFocuse()

        dummySendCode("123456")
        val otpViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[OtpViewModel::class.java]

        val loginSignupViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[LoginSignupViewModel::class.java]

        otpViewModel.getOtpCOde()

        otpViewModel.otpCode.observe(this) {
            Log.d("TAG", "otp code " + it)
            if (it != null) {
                DUMMY_CODE = it
            } else {
                otpViewModel.getOtpCOde()
            }
        }



        otpViewModel.isLoading.observe(this) {
            Log.d("TAG", "loading " + it.toString())
            setLoadingVisible(it)
        }

        loginSignupViewModel.getUserAccountDataDb().observe(this) {
            if (it != null) {
                mAccountData = it
                if (it.otpCode == null) {
                    binding.tvEmail.text = it.email
                } else {
                    val intent2 = Intent(this, DonorDataActivity::class.java)
                    val intent3 = Intent(this, HomeActivity::class.java)
                    loginSignupViewModel.getUserDonorDataDb().observe(this) {

                        if (it == null) {
                            /*Log.d("TAG", "first null data")
                            counter++
                            if(counter>1){
                                Log.d("TAG","null data")
                                counter=0*/
                            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent2.putExtra("name", mAccountData.name)
                            startActivity(intent2)
                            //}
                        } else {
                            intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent3)

                        }
                    }
                }
            }
        }

        loginSignupViewModel.firebaseUser.observe(this) {
            if (it == null) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }

        binding.btBack.setOnClickListener {
            loginSignupViewModel.signOut()
        }


    }

    private fun setFocus() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (binding.etOtpOne.isEnabled) {
            binding.etOtpSix.requestFocus()
            Log.d("TAG", "1 " + binding.etOtpOne.isEnabled.toString())
            inputMethodManager.showSoftInput(binding.etOtpOne, InputMethodManager.SHOW_FORCED)
        } else if (binding.etOtpTwo.isEnabled) {
            //binding.etOtpFive.requestFocus()
            Log.d("TAG", "2 " + binding.etOtpTwo.isEnabled.toString())

            inputMethodManager.showSoftInput(binding.etOtpTwo, InputMethodManager.SHOW_FORCED)
        } else if (binding.etOtpThree.isEnabled) {
            //binding.etOtpFour.requestFocus()
            Log.d("TAG", "3 " + binding.etOtpThree.isEnabled.toString())

            inputMethodManager.showSoftInput(binding.etOtpThree, InputMethodManager.SHOW_FORCED)
        } else if (binding.etOtpFour.isEnabled) {
            //binding.etOtpThree.requestFocus()
            Log.d("TAG", "4 " + binding.etOtpFour.isEnabled.toString())

            inputMethodManager.showSoftInput(binding.etOtpFour, InputMethodManager.SHOW_FORCED)
        } else if (binding.etOtpFive.isEnabled) {
            Log.d("TAG", "5 " + binding.etOtpFive.isEnabled.toString())

            inputMethodManager.showSoftInput(binding.etOtpFive, InputMethodManager.SHOW_FORCED)
            //binding.etOtpTwo.requestFocus()
        } else {
            Log.d("TAG", "6 " + binding.etOtpSix.isEnabled.toString())

            inputMethodManager.showSoftInput(binding.etOtpSix, InputMethodManager.SHOW_FORCED)
            //binding.etOtpOne.requestFocus()
        }
    }


    private fun setListener() {
        binding.layoutFrameRoot.setOnClickListener {
            val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(binding.layoutFrameRoot.windowToken, 0)


        }

        setTextChangeListener(fromEditText = binding.etOtpOne, targetEditText = binding.etOtpTwo)
        setTextChangeListener(fromEditText = binding.etOtpTwo, targetEditText = binding.etOtpThree)
        setTextChangeListener(fromEditText = binding.etOtpThree, targetEditText = binding.etOtpFour)
        setTextChangeListener(fromEditText = binding.etOtpFour, targetEditText = binding.etOtpFive)
        setTextChangeListener(fromEditText = binding.etOtpFive, targetEditText = binding.etOtpSix)
        setTextChangeListener(fromEditText = binding.etOtpSix, done = {
            //Timer("Check", false).schedule(1000) {
                //verifyOTPCode()
            //}
            binding.etOtpSix.postDelayed({
                verifyOTPCode()
            }, 500)
        })

        setKeyListener(fromEditText = binding.etOtpTwo, backToEdittext = binding.etOtpOne)
        setKeyListener(fromEditText = binding.etOtpThree, backToEdittext = binding.etOtpTwo)
        setKeyListener(fromEditText = binding.etOtpFour, backToEdittext = binding.etOtpThree)
        setKeyListener(fromEditText = binding.etOtpFive, backToEdittext = binding.etOtpFour)
        setKeyListener(fromEditText = binding.etOtpSix, backToEdittext = binding.etOtpFive)


    }

    private fun setLoadingVisible(isVisible: Boolean) {
        binding.layoutLoading.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.layoutOtp.visibility = if (isVisible) View.GONE else View.VISIBLE

    }

    private fun initFocuse() {
        binding.etOtpOne.isEnabled = true
        binding.etOtpOne.postDelayed({
            binding.etOtpOne.requestFocus()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(binding.etOtpOne, InputMethodManager.SHOW_FORCED)
        }, 500)
    }

    private fun reset() {
        binding.apply {
            etOtpOne.isEnabled = true
            etOtpOne.setText("")
            etOtpTwo.isEnabled = false
            etOtpTwo.setText("")
            etOtpThree.isEnabled = false
            etOtpThree.setText("")
            etOtpFour.isEnabled = false
            etOtpFour.setText("")
            etOtpFive.isEnabled = false
            etOtpFive.setText("")
            etOtpSix.isEnabled = false
            etOtpSix.setText("")
        }
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.etOtpOne, InputMethodManager.SHOW_FORCED)
    }

    override fun onBackPressed() {
        val loginSignupViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[LoginSignupViewModel::class.java]
        loginSignupViewModel.signOut()
    }

    private fun setTextChangeListener(
        fromEditText: EditText,
        targetEditText: EditText? = null,
        done: (() -> Unit)? = null
    ) {
        fromEditText.addTextChangedListener {
            it?.let { string ->
                if (string.isNotEmpty()) {
                    targetEditText?.let { editText ->
                        editText.isEnabled = true
                        editText.requestFocus()
                    } ?: run {
                        done?.let { done ->
                            done()
                        }
                    }
                    fromEditText.clearFocus()
                    fromEditText.isEnabled = false

                }
            }
        }
    }

    private fun setKeyListener(fromEditText: EditText, backToEdittext: EditText) {
        fromEditText.setOnKeyListener { _, _, event ->

            if (null != event && KeyEvent.KEYCODE_DEL == event.keyCode) {
                backToEdittext.isEnabled = true
                backToEdittext.requestFocus()
                backToEdittext.setText("")

                fromEditText.clearFocus()
                fromEditText.isEnabled = false
            }
            false
        }

    }

    private fun verifyOTPCode() {

        val userDetailViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[UserDetailViewModel::class.java]

        otpCode = binding.etOtpOne.text.toString().trim() +
                binding.etOtpTwo.text.toString().trim() +
                binding.etOtpThree.text.toString().trim() +
                binding.etOtpFour.text.toString().trim() +
                binding.etOtpFive.text.toString().trim() +
                binding.etOtpSix.text.toString().trim()

        if (otpCode.length != 6) {
            return
        }

        if (otpCode == DUMMY_CODE) {
            makeToast(false, "Verification successful!!")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.layoutFrameRoot.windowToken, 0)
            mAccountData.otpCode = otpCode
            userDetailViewModel.saveUserAccountData(mAccountData)
            return
        }

        makeToast(true, "Code invalid")
        //binding.etOtpOne.isEnabled=true
        reset()

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

    private fun dummySendCode(code: String) {
        if (FirebaseAuth.getInstance().currentUser != null) {
            //_isLoading.value = true
            val code = hashMapOf("otpCode" to code)
            FirebaseAuth.getInstance().currentUser?.email?.let {
                val mEmail = it.replace(".", "")
                FirebaseDatabase.getInstance("https://redmine-350506-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("otp_codes")
                    .child(mEmail).setValue(code)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            //_isLoading.value = false
                            makeToast(false, "Code sent successfully")

                            //executorService.execute { userRoomDatabase.userDao().insertAccountData(data) }

                        } else {
                            //_isLoading.value = false
                            makeToast(true, "Error sending data")

                        }
                    }
            }
        }

    }


    companion object {
        var DUMMY_CODE = ""
    }

}