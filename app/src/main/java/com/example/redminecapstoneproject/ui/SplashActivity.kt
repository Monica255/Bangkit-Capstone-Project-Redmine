package com.example.redminecapstoneproject.ui

import android.content.Intent
import android.os.Bundle
import android.view.ViewPropertyAnimator
import androidx.appcompat.app.AppCompatActivity
import com.example.redminecapstoneproject.databinding.ActivitySplashBinding
import com.example.redminecapstoneproject.ui.loginsignup.LoginActivity

class SplashActivity : AppCompatActivity() {
    private var startSplash: ViewPropertyAnimator? = null
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startSplash = binding.imgLogo.animate().setDuration(splashDelay).alpha(1f).withEndAction {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.apply {
                startActivity(this)
                finish()
            }
        }    }

    override fun onDestroy() {
        startSplash?.cancel()
        super.onDestroy()
    }

    companion object {
        private var splashDelay: Long = 2_500L
    }
}