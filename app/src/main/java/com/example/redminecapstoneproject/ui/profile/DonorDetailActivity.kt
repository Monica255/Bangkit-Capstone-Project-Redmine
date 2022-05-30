package com.example.redminecapstoneproject.ui.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.ActivityDonorDetailBinding
import com.example.redminecapstoneproject.helper.helperDate
import com.example.redminecapstoneproject.ui.testing.DonorDataRoom
import com.example.redminecapstoneproject.ui.testing.RegisAccountDataRoom
import www.sanju.motiontoast.MotionToast
import java.time.LocalDate
import java.util.*

class DonorDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDonorDetailBinding

    private var isHaveDonatedValid = false
        get() {
            checkHaveDonated()
            return field
        }
    private var isHadCovidValid = false
        get() {
            checkHadCovid()
            return field
        }

    private var isVerified:Boolean?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonorDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userDetailViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[UserDetailViewModel::class.java]

        userDetailViewModel.userData.observe(this) {
            if(isVerified!=it.isVerified && isVerified!=null){
                userDetailViewModel._newUserData.value = newUserData(it)

            }
            isVerified=it.isVerified
            if (userDetailViewModel._newUserData.value == null) {
                userDetailViewModel._newUserData.value = newUserData(it)
            }
            if (userDetailViewModel._newUserData.value != null) setData(it)
        }

        userDetailViewModel.message.observe(this) {
            makeToast(it.first, it.second)
        }

        binding.cvPickDateLastDonate.setOnClickListener {
            showDatePicker("last donate")
        }

        binding.cvPickDateRecovery.setOnClickListener {
            showDatePicker("recovery date")
        }

        binding.rgGenderUnverified.setOnCheckedChangeListener { _, i ->
            userDetailViewModel.updateDonorDataRoom(
                Pair(
                    if (i == R.id.rb_male) "male" else "female",
                    "gender"
                )
            )
            setButtonSaveEnable(userDetailViewModel.isDataDifferent())
        }

        binding.rgBloodType.setOnCheckedChangeListener { _, i ->
            userDetailViewModel.updateDonorDataRoom(
                Pair(
                    when (i) {
                        R.id.rb_a -> "a"
                        R.id.rb_ab -> "ab"
                        R.id.rb_b -> "b"
                        else -> "o"
                    }, "bloodType"
                )
            )
            setButtonSaveEnable(userDetailViewModel.isDataDifferent())
        }

        binding.rgRhesus.setOnCheckedChangeListener { _, i ->
            userDetailViewModel.updateDonorDataRoom(
                Pair(
                    when (i) {
                        R.id.rb_positive -> "positive"
                        R.id.rb_negative -> "negative"
                        else -> "dont know"
                    }, "rhesus"
                )
            )
            setButtonSaveEnable(userDetailViewModel.isDataDifferent())
        }

        binding.rgHaveYouDonated.setOnCheckedChangeListener { _, i ->
            userDetailViewModel.updateDonorDataRoom(
                Pair(
                    when (i) {
                        R.id.rb_haveDonate_yes -> true
                        else -> false
                    }, "haveDonated"
                )
            )

            if (i == R.id.rb_haveDonate_yes) {
                binding.cvPickDateLastDonate.visibility = View.VISIBLE

                if (userDetailViewModel.temptLastDonateDate != null) {
                    userDetailViewModel.updateDonorDataRoom(
                        Pair(
                            userDetailViewModel.temptLastDonateDate,
                            "lastDonateDate"
                        )
                    )
                    Log.d("TAG", "tmpt date " + userDetailViewModel.temptLastDonateDate.toString())
                    val mTemptLastDonateDate =
                        helperDate.stringToDate(userDetailViewModel.temptLastDonateDate!!)
                    binding.tvDateLastDonate.text = getString(
                        R.string.last_blood_donation,
                        mTemptLastDonateDate.month.toString(),
                        mTemptLastDonateDate.dayOfMonth.toString(),
                        mTemptLastDonateDate.year.toString()
                    )

                } else {
                    binding.tvDateLastDonate.text = getString(
                        R.string.pick_date
                    )
                }
            } else {
                binding.cvPickDateLastDonate.visibility = View.GONE
            }

            setButtonSaveEnable(userDetailViewModel.isDataDifferent())

        }

        binding.rgHaveYouHadCovid.setOnCheckedChangeListener { _, i ->
            userDetailViewModel.updateDonorDataRoom(
                Pair(
                    when (i) {
                        R.id.rb_hadCovid_yes -> true
                        else -> false
                    }, "hadCovid"
                )
            )
            if (i == R.id.rb_hadCovid_yes) {
                binding.cvPickDateRecovery.visibility = View.VISIBLE

                if (userDetailViewModel.temptRecoveryDate != null) {
                    userDetailViewModel.updateDonorDataRoom(
                        Pair(
                            userDetailViewModel.temptRecoveryDate,
                            "recoveryDate"
                        )
                    )
                    val mTemptRecoveryDate =
                        helperDate.stringToDate(userDetailViewModel.temptRecoveryDate!!)
                    Log.d("TAG", "tmpt date2 " + userDetailViewModel.temptRecoveryDate.toString())
                    binding.tvDateRecovery.text = getString(
                        R.string.recovery_date,
                        mTemptRecoveryDate.month.toString(),
                        mTemptRecoveryDate.dayOfMonth.toString(),
                        mTemptRecoveryDate.year.toString()
                    )

                } else {
                    binding.tvDateRecovery.text = getString(
                        R.string.pick_date
                    )
                }
            } else {
                binding.cvPickDateRecovery.visibility = View.GONE
            }

            setButtonSaveEnable(userDetailViewModel.isDataDifferent())

        }


        binding.btBack.setOnClickListener {
            Log.d("LOADING","btn back")
            onBackPressed()
        }

        binding.btSave.setOnClickListener {
            showConfirmDialog("save", userDetailViewModel)
        }
    }

    override fun onBackPressed() {
        Log.d("LOADING","back back")
        val userDetailViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[UserDetailViewModel::class.java]
        if (userDetailViewModel.isDataDifferent()) {
            showConfirmDialog("back", userDetailViewModel)
        } else super.onBackPressed()
    }

    private fun showConfirmDialog(x: String, vm: UserDetailViewModel) {
        val builder = AlertDialog.Builder(this)
        val mConfirmDialog = builder.create()
        if (x == "save") {
            builder.setTitle(getString(R.string.save))
            builder.setMessage("Are you sure you want to save any changes?")
        } else if (x == "back") {
            builder.setTitle(getString(R.string.unsaved_changes))
            builder.setMessage("Are you sure you want to leave without saving?")
        }


        builder.create()


        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            if (x == "back") {
                //vm.tempUserData=vm.InitialuserData
                Log.d("TAG", "ds " + vm.accountData.value.toString())
                vm.userData.value?.let {
                    vm._newUserData.value = newUserData(it)
                    Log.d("TAG", "renew ud " + vm._newUserData.value)
                    setData(it)
                }
                setButtonSaveEnable(false)
                super.onBackPressed()
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
                setButtonSaveEnable(false)
                //save data

            }

        }

        builder.setNegativeButton(getString(R.string.no)) { _, _ ->
            mConfirmDialog.cancel()
        }

        builder.show()
    }


    private fun setButtonSaveEnable(x: Boolean) {
        if (x && isDataValid()) {
            binding.btSave.isEnabled = true
            binding.btSave.alpha = 1F
            //binding.btDiscard.visibility = View.VISIBLE
            Log.d("TAG", "data dif n valid")
        } else if (x) {
            binding.btSave.isEnabled = false
            binding.btSave.alpha = 0.5F
            //binding.btDiscard.visibility = View.VISIBLE
            Log.d("TAG", "data dif n invalid")
        } else {
            binding.btSave.isEnabled = false
            binding.btSave.alpha = 0.5F
            //binding.btDiscard.visibility = View.GONE
            Log.d("TAG", "data same/ invalid")
        }
    }



    private fun setData(user: DonorDataRoom) {
        binding.apply {
            if (user.isVerified) {
                rgGenderUnverified.visibility = View.GONE
                cvGenderVerifiedAccount.visibility = View.VISIBLE
                tvGenderVerifiedAccount.text = user.gender
                layoutVerifiedAccount.visibility = View.VISIBLE
                llayoutUnverifiedAccount.visibility = View.GONE
            } else {
                rgGenderUnverified.visibility = View.VISIBLE
                cvGenderVerifiedAccount.visibility = View.GONE
                layoutVerifiedAccount.visibility = View.GONE
                llayoutUnverifiedAccount.visibility = View.VISIBLE
                rgGenderUnverified.check(if (user.gender == "male") R.id.rb_male else R.id.rb_female)
            }

            rgBloodType.check(
                when (user.bloodType) {
                    "a" -> R.id.rb_a
                    "b" -> R.id.rb_b
                    "ab" -> R.id.rb_ab
                    "o" -> R.id.rb_o
                    else -> -1
                }
            )

            rgRhesus.check(if (user.rhesus == "positive") R.id.rb_positive else if (user.rhesus == "negative") R.id.rb_negative else R.id.rb_dont_know)

            if (user.haveDonated == true) {
                rgHaveYouDonated.check(R.id.rb_haveDonate_yes)
                val mLastDonateDate = user.lastDonateDate?.let { helperDate.stringToDate(it) }
                val month = mLastDonateDate?.month.toString()
                val day = mLastDonateDate?.dayOfMonth.toString()
                val year = mLastDonateDate?.year.toString()

                cvPickDateLastDonate.visibility = View.VISIBLE
                //donateDate = user.lastDonateDate
                tvDateLastDonate.text = getString(R.string.last_blood_donation, month, day, year)
            } else {
                rgHaveYouDonated.check(R.id.rb_haveDonated_no)
                cvPickDateLastDonate.visibility = View.GONE
            }

            if (user.hadCovid == true) {
                rgHaveYouHadCovid.check(R.id.rb_hadCovid_yes)
                val mRecoveryDate = user.recoveryDate?.let { helperDate.stringToDate(it) }
                val month = mRecoveryDate?.month.toString()
                val day = mRecoveryDate?.dayOfMonth.toString()
                val year = mRecoveryDate?.year.toString()

                //recoveryDate = user.recoveryDate
                cvPickDateRecovery.visibility = View.VISIBLE
                tvDateRecovery.text = getString(R.string.recovery_date, month, day, year)
            } else {
                cvPickDateRecovery.visibility = View.GONE
                rgHaveYouHadCovid.check(R.id.rb_hadCovid_no)
            }

        }
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


    private fun isDataValid(): Boolean {
        return isHadCovidValid && isHaveDonatedValid
    }

    private fun checkHaveDonated() {
        val userDetailViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[UserDetailViewModel::class.java]
        val haveDonated = binding.rgHaveYouDonated.checkedRadioButtonId
        val radio: RadioButton = findViewById(haveDonated)
        if (haveDonated == -1) {
            isHaveDonatedValid = false
        } else {
            when (radio.id) {
                R.id.rb_haveDonate_yes -> {
                    isHaveDonatedValid =
                        userDetailViewModel._newUserData.value?.lastDonateDate != null
                }
                R.id.rb_haveDonated_no -> {
                    isHaveDonatedValid = true
                }
            }
        }
    }

    private fun checkHadCovid() {
        val userDetailViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[UserDetailViewModel::class.java]

        val hadCovid = binding.rgHaveYouHadCovid.checkedRadioButtonId
        if (hadCovid == -1) {
            isHadCovidValid = false
        } else {
            val radio: RadioButton = findViewById(hadCovid)
            when (radio.id) {
                R.id.rb_hadCovid_yes -> {
                    isHadCovidValid = userDetailViewModel._newUserData.value?.recoveryDate != null
                }
                R.id.rb_hadCovid_no -> {
                    isHadCovidValid = true
                }
            }
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


    private fun showDatePicker(dateFor: String) {

        val userDetailViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(this)
        )[UserDetailViewModel::class.java]

        var donateDate = userDetailViewModel._newUserData.value?.lastDonateDate?.let {
            helperDate.stringToDate(
                it
            )
        }
        var recoveryDate = userDetailViewModel._newUserData.value?.recoveryDate?.let {
            helperDate.stringToDate(
                it
            )
        }

        val c = Calendar.getInstance()
        var showYear = c.get(Calendar.YEAR)
        var showMonth = c.get(Calendar.MONTH)
        var showDay = c.get(Calendar.DAY_OF_MONTH)

        if (dateFor == "last donate" && donateDate != null) {
            showYear = donateDate.year
            showMonth = helperDate.toNumberMonthFormat(donateDate.month!!, this) - 1
            showDay = donateDate.dayOfMonth

        } else if (dateFor == "recovery date" && recoveryDate != null) {
            showYear = recoveryDate.year
            showMonth = helperDate.toNumberMonthFormat(recoveryDate.month!!, this) - 1
            showDay = recoveryDate.dayOfMonth
        }

        val dpd = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val newMonth = monthOfYear + 1
                if (dateFor == "last donate") {
                    donateDate = LocalDate.of(year, newMonth, dayOfMonth)
                    userDetailViewModel.updateDonorDataRoom(Pair(donateDate, "lastDonateDate"))
                    binding.tvDateLastDonate.apply {
                        text = getString(
                            R.string.last_blood_donation,
                            helperDate.toMonthFormat(newMonth.toString(), this@DonorDetailActivity),
                            dayOfMonth.toString(),
                            year.toString()
                        )
                        alpha = 1F
                    }
                    setButtonSaveEnable(userDetailViewModel.isDataDifferent())
                } else {
                    recoveryDate = LocalDate.of(year, newMonth, dayOfMonth)
                    userDetailViewModel.updateDonorDataRoom(Pair(recoveryDate, "recoveryDate"))
                    binding.tvDateRecovery.apply {
                        text = getString(
                            R.string.recovery_date,
                            helperDate.toMonthFormat(newMonth.toString(), this@DonorDetailActivity),
                            dayOfMonth.toString(),
                            year.toString()
                        )
                        alpha = 1F
                    }
                    setButtonSaveEnable(userDetailViewModel.isDataDifferent())
                }


            },
            showYear,
            showMonth,
            showDay
        )

        c.add(Calendar.MONTH, -3)

        dpd.datePicker.minDate = c.timeInMillis

        dpd.datePicker.maxDate = System.currentTimeMillis()
        dpd.show()
    }


}