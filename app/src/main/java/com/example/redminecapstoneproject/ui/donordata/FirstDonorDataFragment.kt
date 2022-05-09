package com.example.redminecapstoneproject.ui.donordata

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.FragmentFirstDonorDataBinding

class FirstDonorDataFragment : Fragment() {
    private var _binding: FragmentFirstDonorDataBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btContinue.setOnClickListener {view->
            view.findNavController().navigate(R.id.action_firstDonorDataFragment_to_secondDonorDataFragment)
            Log.d("NAV","nav clicked")

        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentFirstDonorDataBinding.inflate(inflater,container,false)
        val view =binding.root
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    companion object {
    }
}