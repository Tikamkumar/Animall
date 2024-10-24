package com.online.animall.activity

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.github.dhaval2404.imagepicker.ImagePicker
import com.online.animall.R
import com.online.animall.data.local.UserPreferences
import com.online.animall.databinding.ActivityNewPostBinding
import com.online.animall.presentation.viewmodel.AnimalViewModel
import com.online.animall.presentation.viewmodel.PostViewModel
import com.online.animall.utils.SnackbarUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class NewPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewPostBinding
    private lateinit var fetchImgs: MutableList<Uri>
    private lateinit var text: String
    private val viewModel: PostViewModel by viewModels()
    private lateinit var userPrefs: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchImgs = mutableListOf()
        userPrefs = UserPreferences(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = getString(R.string.new_post)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.selectPhoto.setOnClickListener {
            fetchPhoto()
        }

        binding.cancleBtn.setOnClickListener {
            fetchImgs.clear()
            showOrHide()
        }

        binding.submitBtn.setOnClickListener {
            uploadPost()
        }
    }

    private fun uploadPost() {

        text = binding.postInfo.text.toString()
        if(text.isEmpty() && fetchImgs.isEmpty()) {
            SnackbarUtil.error(binding.main, "Write Something or Choose Photo")
            return
        }
        try {
            showHideProgress()
            val textRequest = text.toRequestBody("text/plain".toMediaTypeOrNull())
            var type = "image"
            if(getFiles().isEmpty()) type="text"
            val dataTypeRequest = type.toRequestBody("text/plain".toMediaTypeOrNull())
            viewModel.createPost(userPrefs.getToken()!!, getFiles(), textRequest, dataTypeRequest, object: AnimalViewModel.ResponseCallback {
                override fun onSuccess(response: Response<ResponseBody>) {
                    SnackbarUtil.success(binding.main, "Post Successfully..")
                    showHideProgress()
                }
                override fun onError(error: String) {
                    SnackbarUtil.error(binding.main, error)
                    showHideProgress()
                }
            })
        } catch (exp: Exception) {
            showHideProgress()
            SnackbarUtil.error(binding.main)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fetchPhoto() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .createIntent { intent -> startForProfileImageResult.launch(intent) }
    }


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val uri = data?.data!!
                    try {
                        val validMimeTypes = listOf("jpeg", "png", "jpg")
                        Log.i("File: Type: ", uri.toString())
                        if(uri.toString().substringAfterLast(".") in validMimeTypes) {
                            fetchImgs.add(uri)
                            showOrHide()
                        } else {
                            SnackbarUtil.error(binding.root, "Invalid Image format..")
                        }
                    } catch (exp: Exception) {
                        Log.e("Error: ", exp.message.toString())
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun showOrHide() {
        if(fetchImgs.isNotEmpty()) {
            binding.img.setImageURI(fetchImgs[0])
            binding.photoLayout.visibility = View.VISIBLE
            binding.selectPhoto.visibility = View.GONE
        } else {
            binding.photoLayout.visibility = View.GONE
            binding.selectPhoto.visibility = View.VISIBLE
        }
    }

    private fun getFiles(): List<MultipartBody.Part> {
        val list: MutableList<MultipartBody.Part> = mutableListOf()
        for(item in fetchImgs) {
            val fileDir = applicationContext.filesDir
            val file = File(fileDir, item.toString().substringAfterLast("/"))
            val inputStream = contentResolver.openInputStream(item)
            val outputStream = FileOutputStream(file)
            inputStream!!.copyTo(outputStream)
            if(file.exists()) Log.i("File : ", "File Exists..., ${file.name}")
            val requestBody = file.asRequestBody(getMimeType(file).toMediaTypeOrNull())
            val multipart = MultipartBody.Part.createFormData("image", file.name, requestBody)
            list.add(multipart)
        }
        return list.toList()
    }

    private fun getMimeType(file: File): String {
        return when (file.extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            else -> "application/octet-stream" // Fallback for unknown types
        }
    }

    private fun showHideProgress() {
        if(binding.progress.visibility == View.GONE) {
            binding.progress.visibility = View.VISIBLE
            binding.submitBtn.visibility = View.GONE
        } else {
            binding.progress.visibility = View.GONE
            binding.submitBtn.visibility = View.VISIBLE
        }
    }
}