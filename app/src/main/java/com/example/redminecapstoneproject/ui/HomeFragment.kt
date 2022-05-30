package com.example.redminecapstoneproject.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.redminecapstoneproject.LoadingUtils
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.FragmentHomeBinding
import com.example.redminecapstoneproject.ui.blooddonation.BloodDonationActivity
import com.example.redminecapstoneproject.ui.blooddonation.BloodDonorsActivity
import com.example.redminecapstoneproject.ui.createdonorreq.CreateDonorReqActivity
import com.example.redminecapstoneproject.ui.loginsignup.LoginSignupViewModel
import com.example.redminecapstoneproject.ui.testing.DonorDataRoom
import com.example.redminecapstoneproject.ui.testing.RegisAccountDataRoom


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var userAccountData: RegisAccountDataRoom? = null

    override fun onStart() {
        super.onStart()
        if(isAdded){
            val activity= activity as HomeActivity
            activity.state=HomeActivity.HOME
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginSignupViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[LoginSignupViewModel::class.java]



        binding.btDonateBlood.setOnClickListener { view ->
            startActivity(Intent(activity, BloodDonationActivity::class.java))
        }



        binding.btCreateDonorReq.setOnClickListener {
            startActivity(Intent(activity, CreateDonorReqActivity::class.java))

        }

        loginSignupViewModel.getUserAccountDataDb().observe(requireActivity()) {
            if(it!=null){
                val data =
                    RegisAccountDataRoom(it.uid, it.isVerified,it?.name, it?.email, it?.otpCode)
                userAccountData = data
                setData(userAccountData)
            }

        }

        loginSignupViewModel.getUserDonorDataDb().observe(requireActivity()) {
            if(it!=null){
                val data =
                    DonorDataRoom(uid = it.uid, gender = it.gender,province = it.province,city = it.city)
                data.gender?.let { it1 -> setAvatar(it1) }

                binding.btLookForDonors.setOnClickListener {
                    val intent=Intent(activity, BloodDonorsActivity::class.java)
                    intent.putExtra(BloodDonorsActivity.EXTRA_PROVINCE,data.province)
                    intent.putExtra(BloodDonorsActivity.EXTRA_CITY,data.city)
                    startActivity(intent)
                }
            }
        }


    }

    private fun setData(data: RegisAccountDataRoom?) {
        binding.tvName.text = data?.name
    }

    private fun setAvatar(gender: String) {
        val x: Int =if(gender=="male")R.drawable.img_profile_placeholder_male else R.drawable.img_profile_placeholder_female

        if(isAdded){
            Glide.with(this)
                .load(x)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .fallback(R.drawable.ic_launcher_foreground)
                .into(binding.imgProfile)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    companion object {
    }
}