package com.example.redminecapstoneproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ActivityDetailDonationEventsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

class DetailDonationEventsActivity : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDetailDonationEventsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_donation_events)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap=googleMap
        mMap.uiSettings.setAllGesturesEnabled(false)
    }
}