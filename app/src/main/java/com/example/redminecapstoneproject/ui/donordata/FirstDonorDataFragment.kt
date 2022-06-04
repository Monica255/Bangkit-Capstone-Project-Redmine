package com.example.redminecapstoneproject.ui.donordata

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.FragmentFirstDonorDataBinding
import www.sanju.motiontoast.MotionToast

class FirstDonorDataFragment : Fragment(), View.OnFocusChangeListener {

    private var _binding: FragmentFirstDonorDataBinding? = null
    private val binding get() = _binding!!
    private var isPhoneNumberValid = false
        get() {
            checkPhoneNumber()
            return field
        }

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
    private var isRhesusValid = false
        get() {
            checkRhesus()
            return field
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etPhoneNumber.onFocusChangeListener = this

        val donorDataViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[DonorDataViewModel::class.java]

        binding.tvHello.text=getString(R.string.halo,donorDataViewModel.name)

        binding.btContinue.setOnClickListener { view ->
            if (isDataValid()) {
                view.findNavController()
                    .navigate(R.id.action_firstDonorDataFragment_to_secondDonorDataFragment)
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstDonorDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object;

    private fun isFieldsEmpty(): Boolean {
        return binding.etPhoneNumber.text.toString().trim() == ""
                && binding.rgGender.checkedRadioButtonId == -1 && binding.rgBloodType.checkedRadioButtonId == -1 && binding.rgRhesus.checkedRadioButtonId == -1

    }

    private fun isDataValid(): Boolean {
        //checkPhoneNumber()
        return isGenderValid && isBloodTypeValid && isRhesusValid && isPhoneNumberValid
    }

    private fun checkPhoneNumber() {
        val donorDataViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[DonorDataViewModel::class.java]
        val selectedPhoneNumber = binding.etPhoneNumber.text.toString().trim()
        if (selectedPhoneNumber.isEmpty()) {
            isPhoneNumberValid = false
            binding.ilPhone.error = getString(R.string.input_phone_number)
        } else if (selectedPhoneNumber.length < 9) {
            isPhoneNumberValid = false
            binding.ilPhone.error = getString(R.string.phone_number_length_invalid)
        } else
            isPhoneNumberValid = true
        donorDataViewModel.donorData.phoneNumber = selectedPhoneNumber
    }

    private fun checkGender() {
        val donorDataViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[DonorDataViewModel::class.java]
        val selectedGender = binding.rgGender.checkedRadioButtonId
        if (selectedGender == -1) {
            isGenderValid = false
        } else {
            val radio: RadioButton = requireView().findViewById(selectedGender)
            isGenderValid = true
            if(radio.id==R.id.rb_male){
                donorDataViewModel.donorData.gender ="male"
            }else{
                donorDataViewModel.donorData.gender ="female"
            }
        }
    }

    private fun checkBloodType() {
        val donorDataViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[DonorDataViewModel::class.java]
        val selectedBloodType = binding.rgBloodType.checkedRadioButtonId
        if (selectedBloodType == -1) {
            isBloodTypeValid = false
        } else {
            val radio: RadioButton = requireView().findViewById(selectedBloodType)
            isBloodTypeValid = true
            if(radio.id==R.id.rb_a){
                donorDataViewModel.donorData.bloodType = "a"
            }else if (radio.id==R.id.rb_b){
                donorDataViewModel.donorData.bloodType = "b"
            }else if (radio.id==R.id.rb_ab){
                donorDataViewModel.donorData.bloodType = "ab"
            }else{
                donorDataViewModel.donorData.bloodType = "o"
            }
        }
    }

    private fun checkRhesus() {
        val donorDataViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[DonorDataViewModel::class.java]
        val selectedRhesus = binding.rgRhesus.checkedRadioButtonId
        if (selectedRhesus == -1) {
            isRhesusValid = false
        } else {
            val radio: RadioButton = requireView().findViewById(selectedRhesus)
            isRhesusValid = true
            if(radio.id==R.id.rb_negative){
                donorDataViewModel.donorData.rhesus = "negative"
            }else if(radio.id==R.id.rb_positive){
                donorDataViewModel.donorData.rhesus = "positive"
            }else{
                donorDataViewModel.donorData.rhesus = "unknown"
            }
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

