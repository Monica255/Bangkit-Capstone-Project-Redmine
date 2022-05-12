package com.example.redminecapstoneproject.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.FragmentProfileBinding
import com.example.redminecapstoneproject.databinding.FragmentSignupBinding
import com.example.redminecapstoneproject.ui.profile.DonorDetailActivity
import com.example.redminecapstoneproject.ui.profile.UserDetailActivity

class ProfileFragment : Fragment(),View.OnFocusChangeListener {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAction()

    }

    private fun setAction(){
        binding.cvUserProfile.setOnClickListener { view->
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

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view    }

    companion object {


    }



    override fun onFocusChange(p0: View?, p1: Boolean) {
        TODO("Not yet implemented")
    }
}