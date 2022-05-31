package com.example.redminecapstoneproject.ui.verifyaccount

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.ActivityVerifyAccountBinding
import com.example.redminecapstoneproject.ui.testing.Verification
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class VerifyAccountActivity : AppCompatActivity() {
    private lateinit var binding:ActivityVerifyAccountBinding
    private var getFile: File? = null
    private var uid:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityVerifyAccountBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val verifyAccountViewModel=ViewModelProvider(
            this,RepoViewModelFactory.getInstance(this)
        )[VerifyAccountViewModel::class.java]


        setContentView(binding.root)

        uid=intent.getStringExtra(UID)

        binding.btBack.setOnClickListener {
            onBackPressed()
        }

        binding.btGallery.setOnClickListener {
            startGallery()
        }

    }
    private val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    private fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }


    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@VerifyAccountActivity)
            getFile = myFile
            binding.imgPhoto.setImageURI(selectedImg)
            binding.ltJavrvis.visibility=View.GONE
            binding.imgPhoto.visibility=View.VISIBLE
            binding.btVerifyAccount.visibility=View.VISIBLE
            //binding.etDes.requestFocus()
        }
    }



    private fun uploadImage() {
        val verifyAccountViewModel=ViewModelProvider(
            this,RepoViewModelFactory.getInstance(this)
        )[VerifyAccountViewModel::class.java]
        when {
            getFile == null -> {
                Toast.makeText(
                    this@VerifyAccountActivity,
                    getString(R.string.input_picture),
                    Toast.LENGTH_SHORT
                ).show()


            }
            uid == null -> {
                Toast.makeText(
                    this@VerifyAccountActivity,
                    getString(R.string.email_null),
                    Toast.LENGTH_SHORT
                ).show()


            }
            else -> {
                val file = getFile as File
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                verifyAccountViewModel.requestVerification(
                    Verification(
                        imageMultipart,
                        uid!!
                    )
                )

            }
        }


    }


    companion object {
        const val UID=""
        const val FILENAME_FORMAT = "MMddyyyy"
    }

}