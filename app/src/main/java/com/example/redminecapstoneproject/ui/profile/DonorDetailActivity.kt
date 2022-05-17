package com.example.redminecapstoneproject.ui.profile

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ActivityDonorDetailBinding
import com.example.redminecapstoneproject.helper.helperDate
import com.example.redminecapstoneproject.ui.testing.UserData
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonorDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userDetailViewModel =
            ViewModelProvider(this)[UserDetailViewModel::class.java]

        userDetailViewModel.userData.observe(this) {
            //userDetail = it
            setData(it)
            if (userDetailViewModel._newUserData.value == null) {
                userDetailViewModel._newUserData.value = newUserData(it)
            }
        }
        binding.cvPickDateLastDonate.setOnClickListener {
            showDatePicker("last donate")
        }

        binding.cvPickDateRecovery.setOnClickListener {
            showDatePicker("recovery date")
        }

        binding.rgGenderUnverified.setOnCheckedChangeListener { radioGroup, i ->
            userDetailViewModel.updateUserDetail(
                Pair(if (i == R.id.rb_male) "male" else "female",
                "gender")
            )
            setButtonSaveEnable(userDetailViewModel.isDataDifferent())
        }

        binding.rgBloodType.setOnCheckedChangeListener { radioGroup, i ->
            userDetailViewModel.updateUserDetail(
                Pair(when (i) {
                    R.id.rb_a -> "a"
                    R.id.rb_ab -> "ab"
                    R.id.rb_b -> "b"
                    else -> "o"
                }, "bloodType")
            )
            setButtonSaveEnable(userDetailViewModel.isDataDifferent())
        }

        binding.rgRhesus.setOnCheckedChangeListener { radioGroup, i ->
            userDetailViewModel.updateUserDetail(
                Pair(when (i) {
                    R.id.rb_positive -> "positive"
                    R.id.rb_negative -> "negative"
                    else -> "dont know"
                }, "rhesus")
            )
            setButtonSaveEnable(userDetailViewModel.isDataDifferent())
        }

        binding.rgHaveYouDonated.setOnCheckedChangeListener { radioGroup, i ->
            userDetailViewModel.updateUserDetail(
                Pair(when (i) {
                    R.id.rb_haveDonate_yes -> true
                    else -> false
                }, "haveDonated")
            )

            if (i == R.id.rb_haveDonate_yes) {
                binding.cvPickDateLastDonate.visibility = View.VISIBLE

                if (userDetailViewModel.temptLastDonateDate != null) {
                    userDetailViewModel.updateUserDetail(
                        Pair(userDetailViewModel.temptLastDonateDate,
                        "lastDonateDate")
                    )
                    Log.d("TAG", "tmpt date " + userDetailViewModel.temptLastDonateDate.toString())
                    binding.tvDateLastDonate.text = getString(
                        R.string.last_blood_donation,
                        userDetailViewModel.temptLastDonateDate!!.month.toString(),
                        userDetailViewModel.temptLastDonateDate!!.dayOfMonth.toString(),
                        userDetailViewModel.temptLastDonateDate!!.year.toString()
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

        binding.rgHaveYouHadCovid.setOnCheckedChangeListener { radioGroup, i ->
            userDetailViewModel.updateUserDetail(
                Pair(when (i) {
                    R.id.rb_hadCovid_yes -> true
                    else -> false
                }, "hadCovid")
            )
            if (i == R.id.rb_hadCovid_yes) {
                binding.cvPickDateRecovery.visibility = View.VISIBLE

                if (userDetailViewModel.temptRecoveryDate != null) {
                    userDetailViewModel.updateUserDetail(
                        Pair(userDetailViewModel.temptRecoveryDate,
                        "recoveryDate")
                    )
                    Log.d("TAG", "tmpt date2 " + userDetailViewModel.temptRecoveryDate.toString())
                    binding.tvDateRecovery.text = getString(
                        R.string.recovery_date,
                        userDetailViewModel.temptRecoveryDate!!.month.toString(),
                        userDetailViewModel.temptRecoveryDate!!.dayOfMonth.toString(),
                        userDetailViewModel.temptRecoveryDate!!.year.toString()
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
            finish()
        }
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

    private fun setData(user: UserData) {
        binding.apply {
            if (user.isVerified) {
                rgGenderUnverified.visibility = View.GONE
                cvGenderVerifiedAccount.visibility = View.VISIBLE
                tvGenderVerifiedAccount.text = user.gender
            } else {
                rgGenderUnverified.visibility = View.VISIBLE
                cvGenderVerifiedAccount.visibility = View.GONE

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

            rgRhesus.check(if (user.rhesus == "positive") R.id.rb_positive else R.id.rb_negative)

            if (user.haveDonated) {
                rgHaveYouDonated.check(R.id.rb_haveDonate_yes)
                val month = user.lastDonateDate?.month
                val day = user.lastDonateDate?.dayOfMonth.toString()
                val year = user.lastDonateDate?.year.toString()

                cvPickDateLastDonate.visibility = View.VISIBLE
                //donateDate = user.lastDonateDate
                tvDateLastDonate.text = getString(R.string.last_blood_donation, month, day, year)
            } else {
                rgHaveYouDonated.check(R.id.rb_haveDonated_no)
                cvPickDateLastDonate.visibility = View.GONE
            }

            if (user.hadCovid) {
                rgHaveYouHadCovid.check(R.id.rb_hadCovid_yes)
                val month = user.recoveryDate?.month
                val day = user.recoveryDate?.dayOfMonth.toString()
                val year = user.recoveryDate?.year.toString()

                //recoveryDate = user.recoveryDate
                cvPickDateRecovery.visibility = View.VISIBLE
                tvDateRecovery.text = getString(R.string.recovery_date, month, day, year)
            } else {
                cvPickDateRecovery.visibility = View.GONE
                rgHaveYouHadCovid.check(R.id.rb_hadCovid_no)
            }

        }
    }

    private fun newUserData(x: UserData): UserData {
        return UserData(
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
        val userDetailViewModel =
            ViewModelProvider(this)[UserDetailViewModel::class.java]
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
        val userDetailViewModel =
            ViewModelProvider(this)[UserDetailViewModel::class.java]

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

    private fun showDatePicker(dateFor: String) {

        val userDetailViewModel =
            ViewModelProvider(this)[UserDetailViewModel::class.java]

        var donateDate = userDetailViewModel._newUserData.value?.lastDonateDate
        var recoveryDate = userDetailViewModel._newUserData.value?.recoveryDate

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
                    userDetailViewModel.updateUserDetail(Pair(donateDate, "lastDonateDate"))
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
                    userDetailViewModel.updateUserDetail(Pair(recoveryDate, "recoveryDate"))
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