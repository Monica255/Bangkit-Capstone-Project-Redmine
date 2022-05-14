package com.example.redminecapstoneproject.ui.profile

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ActivityDonorDetailBinding
import com.example.redminecapstoneproject.helper.helperDate
import com.example.redminecapstoneproject.ui.testing.DonorDetail
import com.example.redminecapstoneproject.ui.testing.UserDetail
import com.example.redminecapstoneproject.ui.testing.test
import java.time.LocalDate
import java.util.*

class DonorDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDonorDetailBinding
    private lateinit var dialogView: View

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
    private var donateDate: LocalDate? = null
    private var recoveryDate: LocalDate? = null
    private lateinit var donorDetail: DonorDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonorDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        donorDetail = test.generateDummyDonorDetail()
        setData(donorDetail)

        binding.cvPickDateLastDonate.setOnClickListener {
            showDatePicker("last donate")
        }

        binding.cvPickDateRecovery.setOnClickListener {
            showDatePicker("recovery date")
        }

        binding.rgGenderUnverified.setOnCheckedChangeListener { radioGroup, i ->
            setButtonSaveEnable(isDataDifferent())
        }

        binding.rgBloodType.setOnCheckedChangeListener { radioGroup, i ->
            setButtonSaveEnable(isDataDifferent())
        }

        binding.rgRhesus.setOnCheckedChangeListener { radioGroup, i ->
            setButtonSaveEnable(isDataDifferent())
        }

        binding.rgHaveYouDonated.setOnCheckedChangeListener { radioGroup, i ->
            setButtonSaveEnable(isDataDifferent())
            val radio: RadioButton = findViewById(binding.rgHaveYouDonated.checkedRadioButtonId)
            binding.cvPickDateLastDonate.visibility =
                if (radio.id == R.id.rb_haveDonate_yes) View.VISIBLE else View.GONE
        }

        binding.rgHaveYouHadCovid.setOnCheckedChangeListener { radioGroup, i ->
            setButtonSaveEnable(isDataDifferent())
            val radio: RadioButton = findViewById(binding.rgHaveYouHadCovid.checkedRadioButtonId)
            binding.cvPickDateRecovery.visibility =
                if (radio.id == R.id.rb_hadCovid_yes) View.VISIBLE else View.GONE
        }


        binding.btBack.setOnClickListener {
            finish()
        }
    }

    private fun setButtonSaveEnable(x: Boolean) {
        if (x) {
            binding.btSave.isEnabled = true
            binding.btSave.alpha = 1F
            //binding.btDiscard.visibility=View.VISIBLE
            Log.d("TAG", "data dif")
        } else {
            binding.btSave.isEnabled = false
            binding.btSave.alpha = 0.5F
            //binding.btDiscard.visibility=View.GONE
            Log.d("TAG", "data same")
        }
    }

    private fun setData(user: DonorDetail) {
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

            if (donorDetail.haveDonated) {
                rgHaveYouDonated.check(R.id.rb_haveDonate_yes)
                val month = user.lastDonateDate?.month
                val day = user.lastDonateDate?.dayOfMonth.toString()
                val year = user.lastDonateDate?.year.toString()

                cvPickDateLastDonate.visibility = View.VISIBLE
                donateDate = user.lastDonateDate
                tvDateLastDonate.text = getString(R.string.last_blood_donation, month, day, year)
            } else {
                rgHaveYouDonated.check(R.id.rb_haveDonated_no)
                cvPickDateLastDonate.visibility = View.GONE
            }

            if (donorDetail.hadCovid) {
                rgHaveYouHadCovid.check(R.id.rb_hadCovid_yes)
                val month = user.recoveryDate?.month
                val day = user.recoveryDate?.dayOfMonth.toString()
                val year = user.recoveryDate?.year.toString()

                recoveryDate = user.recoveryDate
                cvPickDateRecovery.visibility = View.VISIBLE
                tvDateRecovery.text = getString(R.string.recovery_date, month, day, year)
            } else {
                cvPickDateRecovery.visibility = View.GONE
                rgHaveYouHadCovid.check(R.id.rb_hadCovid_no)
            }

        }
    }

    private fun isDataDifferent(): Boolean {
        val gender: String = if (donorDetail.isVerified) {
            donorDetail.gender
        } else {
            val radio: RadioButton =
                this.findViewById(binding.rgGenderUnverified.checkedRadioButtonId)
            radio.text.toString().lowercase()
        }

        val radioBloodType: RadioButton =
            this.findViewById(binding.rgBloodType.checkedRadioButtonId)
        val bloodType: String = when (radioBloodType.id) {
            R.id.rb_a -> "a"
            R.id.rb_b -> "b"
            R.id.rb_ab -> "ab"
            else -> "o"

        }

        val radioRhesus: RadioButton = this.findViewById(binding.rgRhesus.checkedRadioButtonId)
        val rhesus: String = when (radioRhesus.id) {
            R.id.rb_positive -> "positive"
            R.id.rb_negative -> "negative"
            else -> "dont know"
        }

        val radioHaveDonated: RadioButton =
            this.findViewById(binding.rgHaveYouDonated.checkedRadioButtonId)
        val haveDonated = radioHaveDonated.id == R.id.rb_haveDonate_yes
        val lastDateDonate: LocalDate? = if (haveDonated) donateDate else null

        val radioHadCovid: RadioButton =
            this.findViewById(binding.rgHaveYouHadCovid.checkedRadioButtonId)
        val hadCovid = radioHadCovid.id == R.id.rb_hadCovid_yes
        val recoveryDate: LocalDate? = if (hadCovid) recoveryDate else null


        val newData = DonorDetail(
            donorDetail.isVerified,
            gender,
            bloodType,
            rhesus,
            haveDonated,
            hadCovid,
            lastDateDonate,
            recoveryDate
        )

        Log.d("TAG", "new " + newData.toString())
        Log.d("TAG", "old " + donorDetail)
        return newData != donorDetail
    }


    private fun checkHaveDonated() {
        val haveDonated = binding.rgHaveYouDonated.checkedRadioButtonId
        val radio: RadioButton = findViewById(haveDonated)
        if (haveDonated == -1) {
            isHaveDonatedValid = false
        } else {
            when (radio.id) {
                R.id.rb_haveDonate_yes -> {
                    isHaveDonatedValid = donateDate != null
                }
                R.id.rb_haveDonated_no -> {
                    isHaveDonatedValid = true
                }
            }
        }
    }

    private fun checkHadCovid() {
        val hadCovid = binding.rgHaveYouHadCovid.checkedRadioButtonId
        val radio: RadioButton = findViewById(hadCovid)
        if (hadCovid == -1) {
            isHadCovidValid = false
        } else {
            when (radio.id) {
                R.id.rb_hadCovid_yes -> {
                    isHadCovidValid = recoveryDate != null
                }
                R.id.rb_hadCovid_no -> {
                    isHadCovidValid = true
                }
            }
        }
    }

    private fun showDatePicker(dateFor: String) {
        val c = Calendar.getInstance()
        var showYear = c.get(Calendar.YEAR)
        var showMonth = c.get(Calendar.MONTH)
        var showDay = c.get(Calendar.DAY_OF_MONTH)

        if (dateFor == "last donate" && donateDate != null) {
            showYear = donateDate?.year!!
            showMonth = helperDate.toNumberMonthFormat(donateDate?.month!!, this) - 1
            showDay = donateDate?.dayOfMonth!!

        } else if (dateFor == "recovery date" && recoveryDate != null) {
            showYear = recoveryDate?.year!!
            showMonth = helperDate.toNumberMonthFormat(recoveryDate?.month!!, this) - 1
            showDay = recoveryDate?.dayOfMonth!!
        }

        val dpd = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                var newMonth = monthOfYear + 1
                if (dateFor == "last donate") {
                    /*if(donateDate!=null){
                        showYear = donateDate?.year!!
                        showMonth = helperDate.toNumberMonthFormat(donateDate?.month!!,this)
                        showDay = donateDate?.dayOfMonth!!
                    }*/
                    donateDate = LocalDate.of(year, newMonth, dayOfMonth)
                    binding.tvDateLastDonate.apply {
                        text = getString(
                            R.string.last_blood_donation,
                            helperDate.toMonthFormat(newMonth.toString(), this@DonorDetailActivity),
                            dayOfMonth.toString(),
                            year.toString()
                        )
                        alpha = 1F
                    }
                    setButtonSaveEnable(isDataDifferent())
                } else {
                    /*if(recoveryDate!=null){
                        showYear = recoveryDate?.year!!
                        showMonth = helperDate.toNumberMonthFormat(recoveryDate?.month!!,this)
                        showDay = recoveryDate?.dayOfMonth!!
                    }*/
                    recoveryDate = LocalDate.of(year, newMonth, dayOfMonth)
                    binding.tvDateRecovery.apply {
                        text = getString(
                            R.string.recovery_date,
                            helperDate.toMonthFormat(newMonth.toString(), this@DonorDetailActivity),
                            dayOfMonth.toString(),
                            year.toString()
                        )
                        alpha = 1F
                    }
                    setButtonSaveEnable(isDataDifferent())
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