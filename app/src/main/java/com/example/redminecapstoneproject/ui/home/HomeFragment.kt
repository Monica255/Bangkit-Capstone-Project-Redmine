package com.example.redminecapstoneproject.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.FragmentHomeBinding
import com.example.redminecapstoneproject.repository.Repository
import com.example.redminecapstoneproject.ui.blooddonation.BloodDonationActivity
import com.example.redminecapstoneproject.ui.blooddonation.BloodDonorsActivity
import com.example.redminecapstoneproject.ui.createdonorreq.CreateDonorReqActivity
import com.example.redminecapstoneproject.ui.faq.FaqViewModel
import com.example.redminecapstoneproject.ui.loginsignup.LoginSignupViewModel
import com.example.redminecapstoneproject.ui.testing.DonorDataRoom
import com.example.redminecapstoneproject.ui.testing.FunFact
import com.example.redminecapstoneproject.ui.testing.RegisAccountDataRoom
import java.util.*


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var userAccountData: RegisAccountDataRoom? = null

    override fun onStart() {
        super.onStart()
        val homeViewModel = ViewModelProvider(
            this,
            RepoViewModelFactory.getInstance(requireActivity())
        )[HomeViewModel::class.java]
        if (isAdded) {
            val activity = activity as HomeActivity
            activity.state = HomeActivity.HOME
            val local=Locale.getDefault().language
            if (local == "en") {
                homeViewModel.getAllFunFact(Repository.REF_FF_EN)
            } else if (local == "in") {
                homeViewModel.getAllFunFact(Repository.REF_FF_IN)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) {
            val sharedPref =
                requireActivity().getSharedPreferences("ff", Context.MODE_PRIVATE) ?: return

            val loginSignupViewModel = ViewModelProvider(
                requireActivity(),
                RepoViewModelFactory.getInstance(requireActivity())
            )[LoginSignupViewModel::class.java]

            val homeViewModel = ViewModelProvider(
                this,
                RepoViewModelFactory.getInstance(requireActivity())
            )[HomeViewModel::class.java]



            homeViewModel.getAllFunFactDb.observe(requireActivity()) {
                val local = Locale.getDefault().language
                if (it.isNotEmpty()) {
                    if(it[0].language==local){
                        val oldFF: String? = sharedPref.getString("fun_fact", null)
                        Log.d("EVT","old "+oldFF)
                        if (oldFF == null) {
                            val number = (1 until it.size).random()
                            val ff = it[number - 1].fact
                            with(sharedPref.edit()) {
                                putString("fun_fact", ff)
                                apply()
                            }
                            val new: String? = sharedPref.getString("fun_fact", null)
                            binding.tvFunFact.text = new
                        } else {
                            binding.tvFunFact.text = oldFF
                        }
                    }
                }

            }



            binding.btDonateBlood.setOnClickListener {
                startActivity(Intent(activity, BloodDonationActivity::class.java))
            }



            binding.btCreateDonorReq.setOnClickListener {
                startActivity(Intent(activity, CreateDonorReqActivity::class.java))

            }

            loginSignupViewModel.getUserAccountDataDb().observe(requireActivity()) {
                if (it != null) {
                    val data =
                        RegisAccountDataRoom(it.uid, it.isVerified, it.name, it.email, it.otpCode)
                    userAccountData = data
                    setData(userAccountData)
                }

            }

            loginSignupViewModel.getUserDonorDataDb().observe(requireActivity()) {
                if (it != null) {
                    val data =
                        DonorDataRoom(
                            uid = it.uid,
                            gender = it.gender,
                            province = it.province,
                            city = it.city
                        )
                    data.gender?.let { it1 -> setAvatar(it1) }

                    binding.btLookForDonors.setOnClickListener {
                        val intent = Intent(activity, BloodDonorsActivity::class.java)
                        intent.putExtra(BloodDonorsActivity.EXTRA_PROVINCE, data.province)
                        intent.putExtra(BloodDonorsActivity.EXTRA_CITY, data.city)
                        startActivity(intent)
                    }
                }
            }

        }
    }


    private fun setData(data: RegisAccountDataRoom?) {
        binding.tvName.text = data?.name
    }

    private fun setAvatar(gender: String) {
        val x: Int =
            if (gender == "male") R.drawable.img_profile_placeholder_male else R.drawable.img_profile_placeholder_female

        if (isAdded) {
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
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }
}