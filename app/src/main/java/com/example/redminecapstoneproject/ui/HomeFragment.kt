package com.example.redminecapstoneproject.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.FragmentHomeBinding
import com.example.redminecapstoneproject.ui.createdonorreq.CreateDonorReqActivity
import com.example.redminecapstoneproject.ui.loginsignup.LoginSignupViewModel
import com.example.redminecapstoneproject.ui.testing.AccountData
import com.example.redminecapstoneproject.ui.testing.DonorData
import com.example.redminecapstoneproject.ui.testing.RegisAccountData


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var userAccountData: RegisAccountData? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginSignupViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[LoginSignupViewModel::class.java]


        binding.btDonateBlood.setOnClickListener { view->
            startActivity(Intent(activity, BloodDonationActivity::class.java))
        }

        binding.btLookForDonors.setOnClickListener {
            startActivity(Intent(activity, BloodDonorsActivity::class.java))
        }

        binding.btCreateDonorReq.setOnClickListener {
            startActivity(Intent(activity, CreateDonorReqActivity::class.java))

        }

        loginSignupViewModel.getUserAccountData().observe(requireActivity()) {
            val data =
                RegisAccountData(it?.isVerified, it?.name, it?.email)
            userAccountData = data
            setData(userAccountData)

        }

    }

    private fun setData(data:RegisAccountData?){
        if(data!=null){
            binding.tvName.text=data.name
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