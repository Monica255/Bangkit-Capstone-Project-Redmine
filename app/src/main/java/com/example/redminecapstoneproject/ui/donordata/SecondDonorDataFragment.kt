package com.example.redminecapstoneproject.ui.donordata

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.CustomDialogFragment
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.FragmentSecondDonorDataBinding
import com.example.redminecapstoneproject.helper.helperDate
import com.example.redminecapstoneproject.ui.HomeActivity
import www.sanju.motiontoast.MotionToast
import java.time.LocalDate
import java.util.*


class SecondDonorDataFragment : Fragment() {

    private var _binding: FragmentSecondDonorDataBinding? = null
    private val binding get() = _binding!!
    private var arg = Bundle()
    private var isProvinceValid = false
        get() {
            field = binding.tvProvince.text.toString().trim() != "Province"
            return field
        }
    private var isCityValid = false
        get() {
            field = binding.tvCity.text.toString().trim() != "City"
            return field
        }
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

    private fun setData() {
        val donorDataViewModel =
            ViewModelProvider(requireActivity())[DonorDataViewModel::class.java]
        if (donorDataViewModel.donorData.province != null) {
            binding.tvProvince.apply {
                text = donorDataViewModel.donorData.province
                alpha = 1F
            }
        }
        if (donorDataViewModel.donorData.city != null) {
            binding.tvCity.apply {
                text = donorDataViewModel.donorData.city
                alpha = 1F
            }
        }
        Log.d("TAG", donorDataViewModel.donorData.haveDonated.toString())
        if (donorDataViewModel.donorData.haveDonated != null) {
            val id =
                if (donorDataViewModel.donorData.haveDonated!!) R.id.rb_haveDonate_yes else R.id.rb_haveDonated_no
            //binding.rgHaveYouDonated.check(id)
            if (id == R.id.rb_haveDonate_yes) {
                binding.rbHaveDonateYes.isChecked = true
                binding.cvPickDateLastDonate.visibility = View.VISIBLE

            } else {
                binding.apply {
                    rbHaveDonatedNo.isChecked = true
                    cvPickDateLastDonate.visibility = View.GONE
                    tvDateLastDonate.alpha = 0.6F
                }
            }
            if (donorDataViewModel.donorData.lastDonateDate != null) {
                binding.tvDateLastDonate.text = getString(
                    R.string.last_blood_donation,
                    donorDataViewModel.donorData.lastDonateDate?.month,
                    donorDataViewModel.donorData.lastDonateDate?.dayOfMonth.toString(),
                    donorDataViewModel.donorData.lastDonateDate?.year.toString()
                )
                binding.tvDateLastDonate.alpha = 1F
            }
        }

        if (donorDataViewModel.donorData.hadCovid != null) {
            val id =
                if (donorDataViewModel.donorData.hadCovid!!) R.id.rb_hadCovid_yes else R.id.rb_hadCovid_no
            //binding.rgHaveYouDonated.check(id)
            if (id == R.id.rb_hadCovid_yes) {
                binding.rbHadCovidYes.isChecked = true
                binding.cvPickDateRecovery.visibility = View.VISIBLE
            } else {
                binding.apply {
                    tvDateRecovery.alpha = 0.6F
                    rbHadCovidNo.isChecked = true
                    cvPickDateRecovery.visibility = View.GONE
                }
            }
            if (donorDataViewModel.donorData.recoveryDate != null) {
                binding.tvDateRecovery.text = getString(
                    R.string.last_blood_donation,
                    donorDataViewModel.donorData.recoveryDate?.month,
                    donorDataViewModel.donorData.recoveryDate?.dayOfMonth.toString(),
                    donorDataViewModel.donorData.recoveryDate?.year.toString()
                )
                binding.tvDateRecovery.alpha = 1F
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        val donorDataViewModel =
            ViewModelProvider(requireActivity())[DonorDataViewModel::class.java]

        binding.cvPickDateLastDonate.setOnClickListener {
            showDatePicker("last donate")
        }

        binding.cvPickDateRecovery.setOnClickListener {
            showDatePicker("recovery date")
        }

        binding.cvPickProvince.setOnClickListener {
            newDialog("Select Province")
        }

        binding.cvPickCity.setOnClickListener {
            newDialog("Select City")

        }

        binding.rbHaveDonateYes.setOnClickListener {
            donorDataViewModel.donorData.haveDonated = true
            if(donorDataViewModel.tempLastDonateDate!=null){
                donorDataViewModel.donorData.lastDonateDate = donorDataViewModel.tempLastDonateDate
            }
            binding.cvPickDateLastDonate.visibility = View.VISIBLE
        }

        binding.rbHaveDonatedNo.setOnClickListener {
            donorDataViewModel.donorData.haveDonated = false
            donorDataViewModel.donorData.lastDonateDate=null
            binding.cvPickDateLastDonate.visibility = View.GONE
        }

        binding.rbHadCovidYes.setOnClickListener {
            donorDataViewModel.donorData.hadCovid = true
            if(donorDataViewModel.tempRecoveryDate!=null){
                donorDataViewModel.donorData.recoveryDate = donorDataViewModel.tempRecoveryDate
            }
            binding.cvPickDateRecovery.visibility = View.VISIBLE
        }

        binding.rbHadCovidNo.setOnClickListener {
            donorDataViewModel.donorData.hadCovid = false
            donorDataViewModel.donorData.recoveryDate=null
            binding.cvPickDateRecovery.visibility = View.GONE
        }

        binding.btFinish.setOnClickListener {
            val donorDataViewModel =
                ViewModelProvider(requireActivity())[DonorDataViewModel::class.java]
            if (isDataValid()) {
                Log.d("TAG","valid "+donorDataViewModel.donorData.toString())
                val intent = Intent(activity, HomeActivity::class.java)
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

    private fun isFieldsEmpty(): Boolean {
        return binding.tvProvince.text.toString().trim() == "Province"
                && binding.tvCity.text.toString().trim() == "City"
                && binding.rgHaveYouDonated.checkedRadioButtonId == -1
                && binding.rgHaveYouHadCovid.checkedRadioButtonId == -1

    }

    private fun isDataValid(): Boolean {
        Log.d("TAG", isProvinceValid.toString())
        Log.d("TAG", isCityValid.toString())
        Log.d("TAG", isHaveDonatedValid.toString())
        Log.d("TAG", isHadCovidValid.toString())


        return isProvinceValid && isCityValid && isHaveDonatedValid && isHadCovidValid
    }


    private fun checkHaveDonated() {
        val donorDataViewModel =
            ViewModelProvider(requireActivity())[DonorDataViewModel::class.java]
        val haveDonated = binding.rgHaveYouDonated.checkedRadioButtonId
        if (haveDonated == -1) {
            isHaveDonatedValid = false
        } else {
            val radio: RadioButton? = requireView().findViewById(haveDonated)

            when (radio?.id) {
                R.id.rb_haveDonate_yes -> {
                    isHaveDonatedValid = donorDataViewModel.donorData.lastDonateDate != null
                }
                R.id.rb_haveDonated_no -> {
                    isHaveDonatedValid = true
                }
            }
        }
    }

    private fun checkHadCovid() {
        val donorDataViewModel =
            ViewModelProvider(requireActivity())[DonorDataViewModel::class.java]
        val hadCovid = binding.rgHaveYouHadCovid.checkedRadioButtonId
        if (hadCovid == -1) {
            isHadCovidValid = false
        } else {
            val radio: RadioButton = requireView().findViewById(hadCovid)
            when (radio.id) {
                R.id.rb_hadCovid_yes -> {
                    isHadCovidValid = donorDataViewModel.donorData.recoveryDate != null
                }
                R.id.rb_hadCovid_no -> {
                    isHadCovidValid = true
                }
            }
        }
    }

    private fun newDialog(title: String, isProvinceNotNull: Boolean = false): CustomDialogFragment {
        val dialog = CustomDialogFragment()
        dialog.show(requireActivity().supportFragmentManager, "mDialog")
        arg.putString("title", title)
        //arg.putBoolean("isProvinceNotNull", isProvinceNotNull)
        dialog.arguments
        dialog.arguments = arg
        return dialog
    }

    private fun showDatePicker(dateFor: String) {
        val donorDataViewModel =
            ViewModelProvider(requireActivity())[DonorDataViewModel::class.java]

        val c = Calendar.getInstance()
        var showYear = c.get(Calendar.YEAR)
        var showMonth = c.get(Calendar.MONTH)
        var showDay = c.get(Calendar.DAY_OF_MONTH)

        try {
            if (dateFor == "last donate" && donorDataViewModel.donorData.lastDonateDate != null) {
                showYear = donorDataViewModel.donorData.lastDonateDate?.year!!
                showMonth = helperDate.toNumberMonthFormat(
                    donorDataViewModel.donorData.lastDonateDate?.month!!,
                    requireActivity()
                ) - 1
                showDay = donorDataViewModel.donorData.lastDonateDate?.dayOfMonth!!

            } else if (dateFor == "recovery date" && donorDataViewModel.donorData.recoveryDate != null) {
                showYear = donorDataViewModel.donorData.recoveryDate?.year!!
                showMonth = helperDate.toNumberMonthFormat(
                    donorDataViewModel.donorData.recoveryDate?.month!!,
                    requireActivity()
                ) - 1
                showDay = donorDataViewModel.donorData.recoveryDate?.dayOfMonth!!
            }
        } catch (e: Exception) {
            Log.e("ERROR", "Error message: $e")
        }


        val dpd = DatePickerDialog(
            requireActivity(),
            { _, year, monthOfYear, dayOfMonth ->
                val newMonth = monthOfYear + 1
                if (dateFor == "last donate") {
                    //donateDate = LocalDate.of(year, newMonth, dayOfMonth)
                    Log.d(
                        "TAG",
                        "donate date " + donorDataViewModel.donorData.lastDonateDate.toString()
                    )
                    binding.tvDateLastDonate.apply {
                        val newMonth = monthOfYear + 1
                        text = getString(
                            R.string.last_blood_donation,
                            helperDate.toMonthFormat(newMonth.toString(), requireActivity()),
                            dayOfMonth.toString(),
                            year.toString()
                        )
                        donorDataViewModel.apply {
                            donorData.lastDonateDate =
                                LocalDate.of(year, newMonth, dayOfMonth)
                            tempLastDonateDate =
                                LocalDate.of(year, newMonth, dayOfMonth)
                        }

                        alpha = 1F
                    }
                } else {
                    //recoveryDate = LocalDate.of(year, newMonth, dayOfMonth)
                    binding.tvDateRecovery.apply {
                        val newMonth = monthOfYear + 1
                        text = getString(
                            R.string.recovery_date,
                            helperDate.toMonthFormat(newMonth.toString(), requireActivity()),
                            dayOfMonth.toString(),
                            year.toString()
                        )
                        donorDataViewModel.apply {
                            donorData.recoveryDate =
                                LocalDate.of(year, newMonth, dayOfMonth)
                            tempRecoveryDate=LocalDate.of(year, newMonth, dayOfMonth)
                        }

                        alpha = 1F
                    }
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondDonorDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
    }
}