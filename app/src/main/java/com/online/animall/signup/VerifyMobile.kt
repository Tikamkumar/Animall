package com.online.animall.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.online.animall.R
import com.online.animall.databinding.ActivityVerifyMobileBinding
import com.online.animall.model.OtpRequest
import com.online.animall.model.UserRequest
import com.online.animall.network.RetrofitInstance
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class VerifyMobile : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyMobileBinding
    private var phoneNo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVerifyMobileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.root.setOnClickListener {
            startActivity(Intent(this, NameActivity::class.java))
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
                Toast.makeText(applicationContext, "OTP filled", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, NameActivity::class.java))

                /*GlobalScope.launch {
                    try {
                        val request = OtpRequest(binding.pinview.text.toString(), phoneNo!!)
                        val response = RetrofitInstance.api.verifyMobile(request)
                        if (response.isSuccessful && response.code() == 200) {
                            Log.d("Response: ${response.code()} ", response.body()?.toString() ?: "No response body")
                        } else {
                            Log.e("Error", "Response Code: ${response.code()}, Message: ${response.errorBody()?.string()}")
                        }
                    } catch (exp: Exception) {
                        Log.e("Error", exp.toString())
                    }
                }*/
            }

        }
    }
}