package com.example.redminecapstoneproject.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.FragmentProfileBinding
import com.example.redminecapstoneproject.helper.helperDate
import com.example.redminecapstoneproject.ui.donordata.DonorDataActivity
import com.example.redminecapstoneproject.ui.loginsignup.LoginActivity
import com.example.redminecapstoneproject.ui.loginsignup.LoginSignupViewModel
import com.example.redminecapstoneproject.ui.profile.DonorDetailActivity
import com.example.redminecapstoneproject.ui.profile.UserDetailActivity
import com.example.redminecapstoneproject.ui.testing.AccountData
import com.example.redminecapstoneproject.ui.testing.DonorData
import com.example.redminecapstoneproject.ui.testing.RegisAccountData
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate

class ProfileFragment : Fragment(), View.OnFocusChangeListener {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var userAccountData: RegisAccountData? = null
    private var userDonorData: DonorData? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAction()

        val loginSignupViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[LoginSignupViewModel::class.java]


        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

        loginSignupViewModel.firebaseUser.observe(requireActivity()) { fu ->
            if (fu == null) {
                startActivity(intent)
            }
        }

        loginSignupViewModel.getUserDonorData().observe(requireActivity()) {
            if (it != null) {
                val data =
                    DonorData(
                        it?.isVerified,
                        it?.gender,
                        it?.bloodType,
                        it?.rhesus,
                        it?.phoneNumber,
                        it?.province,
                        it?.city,
                        it?.haveDonated,
                        it?.hadCovid,
                        if (it?.lastDonateDate != null) helperDate.stringToDate(it.lastDonateDate!!) else null,
                        if (it?.recoveryDate != null) helperDate.stringToDate(it.recoveryDate!!) else null


                    )
                userDonorData = data
                Log.d("TAG", userDonorData.toString())
                setData(userDonorData,requireActivity())
            }
        }

        //userAccountData = loginSignupViewModel.userAccountData.value!!
        setData(userDonorData,requireContext())


        loginSignupViewModel.getUserAccountData().observe(requireActivity()) {
            if (it != null) {
                val data =
                    RegisAccountData(it?.isVerified, it?.name, it?.email)
                userAccountData = data
                setData(userAccountData,requireContext())
            }

        }
    }

    @SuppressLint("StringFormatMatches")
    private fun setData(data: Any?,c:Context) {
        Log.d("TAG", data.toString())
        when (data) {
            is RegisAccountData -> {
                binding.tvName.text = (data.name).toString()
                binding.tvEmail.text = (data.email).toString()
                binding.tvVerifyState.text =
                    if ((data.isVerified) as Boolean) c.resources.getString(R.string.verified_account) else getString(
                        R.string.unverified_account
                    )
            }
            is DonorData -> {
                if (data.lastDonateDate != null) {
                    binding.lastBloodDonation.text = getString(
                        R.string.date_format,
                        data.lastDonateDate!!.month,
                        data.lastDonateDate!!.dayOfMonth,
                        data.lastDonateDate!!.year
                    )

                    val x: LocalDate = helperDate.canDonateAgain(data.lastDonateDate!!)
                    binding.canDonateAgain.text = getString(
                        R.string.date_format,
                        x.month,
                        x.dayOfMonth,
                        x.year
                    )
                } else {
                    binding.lastBloodDonation.text = "--"
                    binding.canDonateAgain.text = "--"
                }
            }
        }
    }

    private fun setAction() {
        val loginSignupViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[LoginSignupViewModel::class.java]
        binding.cvUserProfile.setOnClickListener { view ->
            startActivity(Intent(activity, UserDetailActivity::class.java))
        }

        binding.cvDonorDetail.setOnClickListener {
            startActivity(Intent(activity, DonorDetailActivity::class.java))
        }

        binding.cvMyDonorReq.setOnClickListener {

        }

        binding.cvLanguageSetting.setOnClickListener {

        }

        binding.cvContactUs.setOnClickListener {

        }

        binding.cvAboutRedmine.setOnClickListener {

        }

        binding.cvSignOut.setOnClickListener {
            loginSignupViewModel.signOut()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    companion object {


    }


    override fun onFocusChange(p0: View?, p1: Boolean) {
        TODO("Not yet implemented")
    }
}