package com.example.redminecapstoneproject.ui.detaildonorreq

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.databinding.FragmentDonorReqContactBinding
import com.example.redminecapstoneproject.helper.helperUserDetail
import com.example.redminecapstoneproject.ui.BloodDonorDetailsActivity

class DonorReqContactFragment : Fragment() {
    private var _binding: FragmentDonorReqContactBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) {
            var detailDonorReqViewModel =
                ViewModelProvider(requireActivity())[DetailDonorReqViewModel::class.java]

            val phoneNumber = detailDonorReqViewModel.phoneNumber
            binding.tvDonorReqPhoneNumber.text=phoneNumber


            binding.btCopy.setOnClickListener {
                phoneNumber?.let { it1 -> copyToClipBoard(it1) }
            }

            binding.btCall.setOnClickListener {
                Log.d("DR", phoneNumber)
                phoneNumber?.let { it1 -> dialPhoneNumber(it1) }

            }

            binding.btWa.setOnClickListener {
                phoneNumber?.let { it1 -> openWhatsApp(it1) }

            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDonorReqContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun copyToClipBoard(phoneNumber: String) {
        Log.d("DR", "copy " + phoneNumber)
        val validPhone = helperUserDetail.toValidPhoneNumber(phoneNumber)
        val clipboard: ClipboardManager? =
            ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
        val clip: ClipData? =
            ClipData.newPlainText(BloodDonorDetailsActivity.PHONE_NUMBER, validPhone)

        if (clip != null) {
            clipboard?.setPrimaryClip(clip)
            if (isAdded) {
                Toast.makeText(requireActivity(), "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openWhatsApp(phoneNumber: String) {
        val validPhone = helperUserDetail.toValidPhoneNumber(phoneNumber)
        val url = "https://api.whatsapp.com/send?phone=$validPhone"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    fun dialPhoneNumber(phoneNumber: String) {
        val validPhone = helperUserDetail.toValidPhoneNumber(phoneNumber)
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$validPhone")
        if (activity?.let { intent.resolveActivity(it.packageManager) } != null) {
            startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_PHONE_NUMBER = "extra_phone_number"
    }

}