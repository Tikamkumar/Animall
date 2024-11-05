package com.online.animall.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager
import com.online.animall.R
import com.online.animall.activity.TakeLocationActivity
import com.online.animall.adapter.SlideImageAdapter
import com.online.animall.data.local.UserPreferences
import com.online.animall.databinding.ActivityNameBinding
import com.online.animall.home.MainActivity
import com.online.animall.presentation.dialog.LoadingDialog
import com.online.animall.presentation.viewmodel.UserViewModel
import com.online.animall.utils.SnackbarUtil

class NameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNameBinding
    private lateinit var loader: LoadingDialog
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var userPreferences: UserPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNameBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        loader = LoadingDialog(this)

        userPreferences = UserPreferences(this)

        binding.main.setOnClickListener {
          startActivity(Intent(this, TakeLocationActivity::class.java))
        }

        binding.proceedBtn.setOnClickListener {
            regMobile()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun regMobile() {
        if(binding.name.text!!.isEmpty()) {
            binding.validationMsg.visibility = View.VISIBLE
        } else {
            loader.show()
            binding.validationMsg.visibility = View.GONE
            try {
                userViewModel.regName(binding.name.text.toString(), userPreferences.getToken()!!,object: UserViewModel.BooleanCallback{
                    override fun onSuccess(response: Boolean) {
                        loader.hide()
                        startActivity(Intent(this@NameActivity, MainActivity::class.java))
                    }
                    override fun onError(error: String) {
                        loader.hide()
                        SnackbarUtil.error(binding.main)
                    }
                })
            } catch(e: Exception) {
                loader.hide()
                SnackbarUtil.error(binding.main)
            }
        }
    }
}