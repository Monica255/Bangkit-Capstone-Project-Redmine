package com.example.redminecapstoneproject.ui.detaildonorreq

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.ui.WebViewActivity
import com.example.redminecapstoneproject.databinding.FragmentCanDonateBloodBinding

class CanDonateBloodFragment : Fragment() {
    private var _binding: FragmentCanDonateBloodBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btYesImSure.setOnClickListener { view ->
            view.findNavController()
                .navigate(R.id.action_canDonateBloodFragment_to_donorReqContactFragment)
        }

        binding.btMoreInfo.setOnClickListener {
            if (isAdded) {
                val intent = Intent(activity, WebViewActivity::class.java)
                startActivity(intent)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCanDonateBloodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object
}