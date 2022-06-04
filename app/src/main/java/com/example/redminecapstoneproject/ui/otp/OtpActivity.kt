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
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.ActivityOtpBinding
import com.example.redminecapstoneproject.ui.home.HomeActivity
import com.example.redminecapstoneproject.ui.donordata.DonorDataActivity
import com.example.redminecapstoneproject.ui.loginsignup.LoginActivity
import com.example.redminecapstoneproject.ui.loginsignup.LoginSignupViewModel
import com.example.redminecapstoneproject.ui.profile.UserDetailViewModel
import com.example.redminecapstoneproject.ui.testing.RegisAccountDataRoom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import www.sanju.motiontoast.MotionToast

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private var otpCode = ""
    private lateinit var mAccountData: RegisAccountDataRoom
    private var startIntent=true
    private var sendOtp=true

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOtpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        //dummySendCode("123456")
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
            if (it != null) {
                OTP_CODE = it
            } else {
                otpViewModel.getOtpCOde()
            }
        }


        otpViewModel.responseOtp.observe(this){
            if (it!=null){
                OTP_CODE=it.otpCode
            }
        }

        otpViewModel.isLoading.observe(this) {
            if(it){
                setLoadingVisible(true)
            }else{
                binding.etOtpOne.postDelayed({
                    setLoadingVisible(false)
                },900)
            }

        }

        loginSignupViewModel.getUserAccountDataDb().observe(this) {
            if (it != null) {
                mAccountData = it
                if (it.otpCode == null && sendOtp) {
                    binding.tvEmail.text = it.email
                    it.email?.let { it1 -> otpViewModel.sendOtpCOde(it1)
                    sendOtp=false
                    }
                } else if (it.otpCode != null){
                    val intent2 = Intent(this, DonorDataActivity::class.java)
                    val intent3 = Intent(this, HomeActivity::class.java)
                    loginSignupViewModel.getUserDonorDataDb().observe(this) {v->

                        if (v == null && startIntent) {
                            /*Log.d("TAG", "first null data")
                            counter++
                            if(counter>1){
                                Log.d("TAG","null data")
                                counter=0*/
                            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent2.putExtra("name", mAccountData.name)
                            startIntent=false
                            startActivity(intent2)
                            //}
                        } else if(startIntent) {
                            intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startIntent=false
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
        if(isVisible){
            binding.layoutLoading.visibility=View.VISIBLE
            binding.layoutOtp.visibility=View.GONE
        }else{
            binding.layoutLoading.visibility=View.GONE
            binding.layoutOtp.visibility=View.VISIBLE
            setListener()
            initFocuse()
        }
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

        if (otpCode == OTP_CODE) {
            Log.d("EVT","same")
            makeToast(false, getString(R.string.verification_successful))
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.layoutFrameRoot.windowToken, 0)
            mAccountData.otpCode = otpCode
            userDetailViewModel.saveUserAccountData(mAccountData)
            return
        }

        makeToast(true, getString(R.string.code_invalid))
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
                getString(R.string.yey_success),
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

    private fun dummySendCode(mCode: String) {
        if (FirebaseAuth.getInstance().currentUser != null) {
            //_isLoading.value = true
            val code = hashMapOf("otpCode" to mCode)
            FirebaseAuth.getInstance().currentUser?.email?.let {
                val mEmail = it.replace(".", "")
                FirebaseDatabase.getInstance("https://redmine-350506-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("otp_codes")
                    .child(mEmail).setValue(code)
                    .addOnCompleteListener {task->
                        if (task.isSuccessful) {
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
        var OTP_CODE = ""
    }

}