package com.example.redminecapstoneproject.ui.donordata

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.redminecapstoneproject.databinding.ActivityDonorDataBinding


class DonorDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDonorDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonorDataBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mFragmentManager = supportFragmentManager
        val mFirstDonorDataFragment = FirstDonorDataFragment()
        val fragment =
            mFragmentManager.findFragmentByTag(FirstDonorDataFragment::class.java.simpleName)
        /*if (fragment !is FirstDonorDataFragment) {
            Log.d("MyFlexibleFragment", "Fragment Name :" + FirstDonorDataFragment::class.java.simpleName)
            mFragmentManager
                .beginTransaction()
                .add(R.id.frame_container, mFirstDonorDataFragment, FirstDonorDataFragment::class.java.simpleName)
                .commit()
        }*/

        binding.btBack.setOnClickListener {
            onBackPressed()
        }
    }
}
