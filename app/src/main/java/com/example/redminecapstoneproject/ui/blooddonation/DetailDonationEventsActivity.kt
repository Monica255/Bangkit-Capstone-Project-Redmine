package com.example.redminecapstoneproject.ui.blooddonation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ActivityDetailDonationEventsBinding
import com.example.redminecapstoneproject.helper.HelperBloodDonors
import com.example.redminecapstoneproject.helper.HelperUserDetail
import com.example.redminecapstoneproject.helper.LocationConverter
import com.example.redminecapstoneproject.ui.testing.DonorEvent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DetailDonationEventsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDetailDonationEventsBinding
    private lateinit var coordinate: LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDonationEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        binding.btBack.setOnClickListener {
            finish()
        }



    }

    private fun openMaps(data:LatLng){
        val intent=Intent(android.content.Intent.ACTION_VIEW,
            Uri.parse("geo:${data.latitude},${data.longitude}?z=20"))
        intent.setPackage("com.google.android.apps.maps")
        intent.resolveActivity(packageManager)?.let {
            startActivity(intent)
        }

    }



    private fun setData(data: DonorEvent) {
        binding.apply {
            Glide.with(baseContext)
                .load(data.image)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .fallback(R.drawable.ic_launcher_foreground)
                .into(binding.imgBannerEvent)
            tvDonationEventName.text = HelperUserDetail.toTitleCase(data.name)
            tvDonationEventTime.text = data.time
            donationEventLocation.text = HelperBloodDonors.toLocation(data.city?.lowercase()
                ?.replaceFirstChar(Char::titlecase),
                data.province?.let {
                    HelperUserDetail.getProvinceName(it).lowercase()
                        .replaceFirstChar(Char::titlecase)
                })
            donationEventPlace.text = data.place
            tvDonationEventDes.text = data.description
            if (data.lat != null && data.lon != null) {
                coordinate = LatLng(data.lat, data.lon)
            }
            val address=LocationConverter.getStringAddress(coordinate, baseContext)
            tvDonationEventAddress.text =address


            val posisition = LocationConverter.toLatlng(data.lat, data.lon)
            if (address!=getString(R.string.no_location)&& posisition!=null &&address!=getString(R.string.location_name_unknown)) {
                binding.cvMap.visibility = View.VISIBLE
                mMap.addMarker(
                    MarkerOptions().position(posisition)
                )
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        posisition, DEFAULT_ZOOM
                    )
                )

            } else {
                binding.cvMap.visibility = View.GONE
            }

            mMap.setOnMapClickListener {
                Log.d("EVT",coordinate.toString())
                if(coordinate!=null) openMaps(coordinate)

            }



        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.setAllGesturesEnabled(false)
        val eventData = intent.getParcelableExtra<DonorEvent>(EXTRA_DONATION_EVENT)
        if (eventData != null) {
            setData(eventData)
        }
    }

    companion object {
        const val EXTRA_DONATION_EVENT = "extra_donation_event"
        const val TAG = "MAP"
        const val DEFAULT_ZOOM = 15f
    }
}