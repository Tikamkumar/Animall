package com.online.animall.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import com.online.animall.R
import com.online.animall.databinding.ActivityEditProfileBinding
import com.online.animall.utils.SnackbarUtil
import java.util.Calendar

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.icCamera.setOnClickListener {
            fetchPhoto()
        }
        binding.pickDob.setOnClickListener {
            datePicker()
        }

    }

    private fun fetchPhoto() {
        ImagePicker.with(this)
            .cropSquare()
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
                        if(uri.toString().substringAfterLast(".") in validMimeTypes) {
                            binding.profileImage.setImageURI(uri)
                           /* fetchImgs.add(uri)
                            imgAdapter.updateList(fetchImgs)
                            Log.e("Size of List : ", fetchImgs.size.toString())*/
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

        private fun datePicker() {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    binding.pickDob.setText("$dayOfMonth/${monthOfYear+1}/$year")
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
}