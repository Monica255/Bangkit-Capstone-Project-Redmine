package com.example.redminecapstoneproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.databinding.ActivityWebViewBinding
import java.util.*

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding:ActivityWebViewBinding
    var isLoading=MutableLiveData<Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityWebViewBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        isLoading.value=true
        val webSettings = binding.wvDonorRequirement.settings
        webSettings.javaScriptEnabled = true

        binding.wvDonorRequirement.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                isLoading.value=false
                Toast.makeText(this@WebViewActivity, getString(R.string.load_web_success), Toast.LENGTH_LONG).show()
            }
        }
        //binding.wvDonorRequirement.loadUrl(getString(R.string.web_url))
        binding.wvDonorRequirement.loadUrl(getString(R.string.web_url))
        Log.d("EMPTY",Locale.getDefault().language)

        isLoading.observe(this){
            showLoading(it)
        }

    }

    private fun showLoading(show:Boolean){

        if(show){
            binding.tvLoadingWeb.visibility=View.VISIBLE
            binding.ltJavrvis.visibility= View.VISIBLE
            binding.wvDonorRequirement.visibility= View.GONE
        }else{
            binding.tvLoadingWeb.visibility=View.GONE
            binding.ltJavrvis.visibility= View.GONE
            binding.wvDonorRequirement.visibility= View.VISIBLE
        }
    }
}