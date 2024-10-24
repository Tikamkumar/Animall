package com.online.animall.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.online.animall.R
import com.online.animall.data.local.UserPreferences
import com.online.animall.data.model.CreateUserRequest
import com.online.animall.data.model.CreateUserResponse
import com.online.animall.data.remote.RetrofitClient
import com.online.animall.databinding.ActivityEnterMobileBinding
import com.online.animall.presentation.dialog.LoadingDialog
import com.online.animall.presentation.viewmodel.AnimalViewModel
import com.online.animall.presentation.viewmodel.UserViewModel
import com.online.animall.utils.SnackbarUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class EnterMobileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEnterMobileBinding
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEnterMobileBinding.inflate(layoutInflater)

        setContentView(binding.root)
        loadingDialog = LoadingDialog(this)

        binding.proceedBtn.setOnClickListener {
            signUp()
        }

        binding.root.setOnClickListener {
            startActivity(Intent(this, VerifyMobile::class.java))
        }
        binding.mobileNumber.addTextChangedListener(textWatcher)
    }

    private fun signUp() {
        CoroutineScope(Dispatchers.IO).launch {

            val response = RetrofitClient.api.getLactation(UserPreferences(applicationContext).getToken()!!)
            if (response.isSuccessful) {
                // Handle the successful response
                val userResponse = response.body()
                if (userResponse != null) {
                    // Process userResponse
                    Log.d("API Success", "Response: $userResponse")
                    /*val intent = Intent(this@EnterMobileActivity, VerifyMobile::class.java)
                    intent.putExtra("mobile", mobileNumber)
                    startActivity(intent)*/
                }
            } else {
                // Handle the error response
                val errorBody = response.errorBody()?.string()
                Log.e("API Error", "Code: ${response.code()}, Body: $errorBody")
            }
        }/*
        loadingDialog.show()
        val mobileNumber = binding.mobileNumber.text.toString()
        if(mobileNumber.isEmpty() || mobileNumber.length != 10) {
            binding.validationMsg.visibility = View.VISIBLE
            loadingDialog.hide()
        } else {
            binding.validationMsg.visibility = View.GONE
            try {
                userViewModel.createUser(mobileNumber, object : UserViewModel.CreateUserCallback {
                    override fun onSuccess(response: CreateUserResponse) {
                        loadingDialog.hide()
                        val intent = Intent(this@EnterMobileActivity, VerifyMobile::class.java)
                        intent.putExtra("mobile", mobileNumber)
                        startActivity(intent)
                    }
                    override fun onError(error: String) {
                        loadingDialog.hide()
                        SnackbarUtil.error(binding.main)
                    }
                })
            } catch(e: Exception) {
                SnackbarUtil.error(binding.main)
            }
        }
    */}

    private var textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            binding.validationMsg.visibility = View.GONE
        }
        override fun afterTextChanged(s: Editable?) {}
    }
}