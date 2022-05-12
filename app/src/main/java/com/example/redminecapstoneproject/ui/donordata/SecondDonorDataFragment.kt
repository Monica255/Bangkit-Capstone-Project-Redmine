package com.example.redminecapstoneproject.ui.donordata

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.redminecapstoneproject.databinding.FragmentFirstDonorDataBinding
import com.example.redminecapstoneproject.databinding.FragmentSecondDonorDataBinding
import com.example.redminecapstoneproject.ui.HomeActivity

class SecondDonorDataFragment : Fragment() {

    private var _binding: FragmentSecondDonorDataBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btFinish.setOnClickListener {
            val intent=Intent(activity,HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSecondDonorDataBinding.inflate(inflater,container,false)
        val view =binding.root
        return view
    }

    companion object {
    }
}