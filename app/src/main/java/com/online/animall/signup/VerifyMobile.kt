package com.online.animall.signup

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.online.animall.R
import com.online.animall.data.local.UserPreferences
import com.online.animall.data.model.CreateUserResponse
import com.online.animall.data.model.OtpResponse
import com.online.animall.databinding.ActivityVerifyMobileBinding
import com.online.animall.presentation.dialog.LoadingDialog
import com.online.animall.presentation.viewmodel.UserViewModel
import com.online.animall.utils.SnackbarUtil

class VerifyMobile : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyMobileBinding
    private var phoneNo: String? = null
    private val userViewModel: UserViewModel by viewModels()
    private var mobile: String? = null
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var userPreference: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityVerifyMobileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = LoadingDialog(this)

        mobile = intent.getStringExtra("mobile")
        binding.otpNumber.text = "Otp sent to $mobile" ?: "null"

        userPreference = UserPreferences(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.root.setOnClickListener {
            startActivity(Intent(this, NameActivity::class.java))
        }

        binding.changeNumber.setOnClickListener {
            val intent = Intent(this, EnterMobileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        binding.resendOtp.setOnClickListener {
            loadingDialog.show()
            binding.pinview.setText("")
            try {
                /*userViewModel.createUserResponse.removeObservers(this@VerifyMobile)
                userViewModel.createUserResponse.observe(this) { response ->
                    loadingDialog.hide()
                    if(response != null) {

                    } else {
                        SnackbarUtil.error(binding.main)
                    }
                }*/
                userViewModel.createUser(phoneNo!!, object : UserViewModel.CreateUserCallback {
                    override fun onSuccess(response: CreateUserResponse) {
                        loadingDialog.hide()
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
        phoneNo = intent.getStringExtra("mobile")
        binding.pinview.addTextChangedListener(textWatcher)
    }

    private var textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }
        override fun afterTextChanged(s: Editable?) {
            if(s!!.length == 6) {
                 try {
                     /*userViewModel.otpResponse.removeObservers(this@VerifyMobile)
                    userViewModel.otpResponse.observe(this@VerifyMobile) { res ->
                       if(res != null) {
                           startActivity(Intent(applicationContext, NameActivity::class.java))
                             userPreference.saveData("Bearer ${res.token.token}", res.token.userId)
                       } else {
                           hideKeyboard()
                           SnackbarUtil.error(binding.main, "Invalid OTP..")
                       }
                     }*/
                     userViewModel.verifyMobile(mobile!!, binding.pinview.text.toString(),object: UserViewModel.VerifyMobileCallback {
                         override fun onSuccess(response: OtpResponse) {
                             startActivity(Intent(applicationContext, NameActivity::class.java))
                             userPreference.saveData("Bearer ${response.token.token}", response.token.userId)
                         }

                         override fun onError(error: String) {
                             hideKeyboard()
                             SnackbarUtil.error(binding.main, "Invalid OTP..")
                         }
                     })
                } catch (e: Exception) {
                     hideKeyboard()
                     SnackbarUtil.error(binding.main)
                }
            }
        }
    }

    fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = currentFocus
        currentFocusView?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

}