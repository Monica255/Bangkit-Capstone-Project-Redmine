package com.example.redminecapstoneproject.ui.detaildonorreq

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.FragmentDonorReqContactBinding
import com.example.redminecapstoneproject.helper.HelperUserDetail
import com.example.redminecapstoneproject.ui.blooddonation.BloodDonorDetailsActivity

class DonorReqContactFragment : Fragment() {
    private var _binding: FragmentDonorReqContactBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) {
            val detailDonorReqViewModel =
                ViewModelProvider(requireActivity())[DetailDonorReqViewModel::class.java]

            val phoneNumber = detailDonorReqViewModel.phoneNumber
            binding.tvDonorReqPhoneNumber.text=phoneNumber


            binding.btCopy.setOnClickListener {
                copyToClipBoard(phoneNumber)
            }

            binding.btCall.setOnClickListener {
                dialPhoneNumber(phoneNumber)

            }

            binding.btWa.setOnClickListener {
                openWhatsApp(phoneNumber)

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

    private fun copyToClipBoard(phoneNumber: String) {
        val validPhone = HelperUserDetail.toValidPhoneNumber(phoneNumber)
        val clipboard: ClipboardManager? =
            ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
        val clip: ClipData? =
            ClipData.newPlainText(BloodDonorDetailsActivity.PHONE_NUMBER, validPhone)

        if (clip != null) {
            clipboard?.setPrimaryClip(clip)
            if (isAdded) {
                Toast.makeText(requireActivity(), getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openWhatsApp(phoneNumber: String) {
        val validPhone = HelperUserDetail.toValidPhoneNumber(phoneNumber)
        val url = "https://wa.me/$validPhone"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun dialPhoneNumber(phoneNumber: String) {
        val validPhone = HelperUserDetail.toValidPhoneNumber(phoneNumber)
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$validPhone")
        if (activity?.let { intent.resolveActivity(it.packageManager) } != null) {
            startActivity(intent)
        }
    }

    companion object

}