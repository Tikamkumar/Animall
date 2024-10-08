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
import com.online.animall.databinding.ActivityEnterMobileBinding
import com.online.animall.model.UserRequest
import com.online.animall.network.RetrofitInstance
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.log

class EnterMobileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEnterMobileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterMobileBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        binding.proceedBtn.setOnClickListener {
            proceedfunc()
        }

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.root.setOnClickListener {
            startActivity(Intent(this, VerifyMobile::class.java))
        }

        binding.mobileNumber.addTextChangedListener(textWatcher)
    }

    private fun proceedfunc() {
        val mobileNumber = binding.mobileNumber.text.toString()
        startActivity(Intent(this, VerifyMobile::class.java))
        if(mobileNumber.isEmpty() || mobileNumber.length != 10) {
            binding.validationMsg.visibility = View.VISIBLE
        } else {
            binding.validationMsg.visibility = View.GONE
            startActivity(Intent(this, VerifyMobile::class.java))
            // launching a new coroutine
            /*GlobalScope.launch {
                try {
                    val user = UserRequest(mobileNumber)
                    val response = RetrofitInstance.api.createUser(user)

                    if (response.isSuccessful && response.code() == 201) {
                        val intent = Intent(applicationContext, VerifyMobile::class.java)
                        intent.putExtra("mobile", "$mobileNumber")
                        startActivity(intent)
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

    private var textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            binding.validationMsg.visibility = View.GONE
        }
        override fun afterTextChanged(s: Editable?) {}
    }
}