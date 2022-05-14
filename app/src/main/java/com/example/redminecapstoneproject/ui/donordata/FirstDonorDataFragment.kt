package com.example.redminecapstoneproject.ui.donordata

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.FragmentFirstDonorDataBinding
import www.sanju.motiontoast.MotionToast

class FirstDonorDataFragment : Fragment(), View.OnFocusChangeListener {
    private var _binding: FragmentFirstDonorDataBinding? = null
    private val binding get() = _binding!!
    private var isPhoneNumberValid= false
    private var isGenderValid = false
    get() {
        checkGender()
        return field
    }
    private var isBloodTypeValid = false
        get() {
            checkBloodType()
            return field
        }
    private var isRhesusValid= false
        get() {
            checkRhesus()
            return field
        }
    private var phoneNumber=""
    private var gender=""
    private var bloodType=""
    private var rhesus=""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etPhoneNumber.onFocusChangeListener = this
        binding.btContinue.setOnClickListener { view ->

            if (isDataValid()) {
                view.findNavController()
                    .navigate(R.id.action_firstDonorDataFragment_to_secondDonorDataFragment)
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
    ): View {
        _binding = FragmentFirstDonorDataBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
    }

    private fun isFieldsEmpty(): Boolean {
        return binding.etPhoneNumber.text.toString().trim() == ""
                && binding.rgGender.checkedRadioButtonId == -1 && binding.rgBloodType.checkedRadioButtonId == -1 && binding.rgRhesus.checkedRadioButtonId == -1

    }

    private fun isDataValid(): Boolean {
        binding.ilPhone.clearFocus()
        return isGenderValid && isBloodTypeValid && isRhesusValid && isPhoneNumberValid
    }

    private fun checkPhoneNumber() {
        val phoneNumber = binding.etPhoneNumber.text.toString().trim()
        if (phoneNumber.isEmpty()) {
            isPhoneNumberValid = false
            binding.ilPhone.error = getString(R.string.input_phone_number)
        } else if (phoneNumber.length < 9) {
            isPhoneNumberValid = false
            binding.ilPhone.error = getString(R.string.phone_number_length_invalid)
        } else
            isPhoneNumberValid = true
    }

    private fun checkGender() {
        val gender = binding.rgGender.checkedRadioButtonId
        if (gender == -1) {
            isGenderValid = false
        } else {
            val radio: RadioButton = requireView().findViewById(gender)
            isGenderValid = true
        }
    }

    private fun checkBloodType() {
        val bloodType = binding.rgBloodType.checkedRadioButtonId
        if (bloodType == -1) {
            isBloodTypeValid = false
        } else {
            val radio: RadioButton = requireView().findViewById(bloodType)
            isBloodTypeValid = true
        }
    }

    private fun checkRhesus() {
        val rhesus = binding.rgRhesus.checkedRadioButtonId
        if (rhesus == -1) { // If any radio button checked from radio group
            // Get the instance of radio button using id
            isRhesusValid = false
        } else {
            val radio: RadioButton = requireView().findViewById(rhesus)
            isRhesusValid = true
            // If no radio button checked in this radio group
        }
    }

    override fun onFocusChange(v: View?, isFocused: Boolean) {
        if (v != null) {
            when (v.id) {
                R.id.et_phoneNumber -> {
                    if (isFocused) {
                        binding.ilPhone.isErrorEnabled = false
                        binding.ilPhone.error = ""
                    } else {
                        checkPhoneNumber()
                    }
                }

            }
        }
    }

}

