package com.example.redminecapstoneproject

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.redminecapstoneproject.adapter.AlertDialogAdapter
import com.example.redminecapstoneproject.databinding.ActivityAlertDialogBinding

class AlertDialogActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAlertDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAlertDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }


}

