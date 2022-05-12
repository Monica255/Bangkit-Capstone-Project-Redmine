package com.example.redminecapstoneproject.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.FragmentHomeBinding
import com.example.redminecapstoneproject.databinding.FragmentProfileBinding
import com.example.redminecapstoneproject.ui.profile.DonorDetailActivity
import com.example.redminecapstoneproject.ui.profile.UserDetailActivity


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btDonateBlood.setOnClickListener { view->
            startActivity(Intent(activity, BloodDonationActivity::class.java))
        }

        binding.btLookForDonors.setOnClickListener {
            startActivity(Intent(activity, BloodDonorsActivity::class.java))
        }

        binding.btCreateDonorReq.setOnClickListener {
            startActivity(Intent(activity, CreateDonorReqActivity::class.java))

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