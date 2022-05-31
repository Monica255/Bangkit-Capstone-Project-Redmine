package com.example.redminecapstoneproject

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import com.example.redminecapstoneproject.databinding.FragmentReqSentBinding

class ReqSentJarvis(context: Context):Dialog(context) {
    private lateinit var binding:FragmentReqSentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= FragmentReqSentBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window?.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawableResource(R.color.white_transparent)

        binding.btOk.setOnClickListener {
            LoadingUtils.hideDialog2()
        }
    }
}