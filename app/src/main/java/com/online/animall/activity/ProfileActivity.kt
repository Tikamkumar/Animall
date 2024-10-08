package com.online.animall.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.online.animall.LocaleHelper
import com.online.animall.LocaleHelper.setLocale
import com.online.animall.databinding.ActivityProfileBinding
import com.online.animall.home.MainActivity
import java.util.Locale

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocaleHelper.getLocale(this)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.customerToolbar)

        binding.icBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.yourAnimalBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("SELECTED_TAB", "2")
            startActivity(intent)
        }

        binding.walletBtn.setOnClickListener {
            startActivity(Intent(this, WalletActivity::class.java))
        }

        binding.edtProfileBtn.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
        binding.choiceAnimalBtn.setOnClickListener {
            startActivity(Intent(this, AnimalForYouActivity::class.java))
        }
        binding.selectAnimalBtn.setOnClickListener {
            startActivity(Intent(this, AnimalForYouActivity::class.java))
        }
        binding.changeLanBtn.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(com.online.animall.R.layout.change_lan_layout, null)
            val btnClose = view.findViewById<ImageView>(com.online.animall.R.id.ic_close)
            val engBtn = view.findViewById<TextView>(com.online.animall.R.id.eng_btn)
            val hinBtn = view.findViewById<TextView>(com.online.animall.R.id.hin_btn)
            btnClose.setOnClickListener {
                dialog.dismiss()
            }
            engBtn.setOnClickListener {
                LocaleHelper.setLocale("en", this)
                dialog.dismiss()
                restartApp()
            }
            hinBtn.setOnClickListener {
                LocaleHelper.setLocale("hi", this)
                dialog.dismiss()
                restartApp()
            }

            dialog.setContentView(view)
            dialog.show()
        }
    }

  fun getLocale() {
      val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
      val savedLanguage = sharedPreferences.getString("language", "hi")

      if (Locale.getDefault().language != savedLanguage) {
          setLocale(savedLanguage ?: "hi", this) // Recreate the activity to apply the new locale
      }
  }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(com.online.animall.R.menu.profile_menu, menu)
        return true
    }

    fun restartApp() {
        val intent = Intent(this, MainActivity::class.java) // Replace MainActivity with your actual main activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}