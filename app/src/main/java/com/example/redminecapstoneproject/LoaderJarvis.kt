package com.example.redminecapstoneproject

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout

class LoaderJarvis(context: Context):Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loader)
        window?.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
            )
        window?.setBackgroundDrawableResource(R.color.white_transparent)
    }
}