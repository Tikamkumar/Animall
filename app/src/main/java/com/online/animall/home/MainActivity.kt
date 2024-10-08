package com.online.animall.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.online.animall.LocaleHelper
import com.online.animall.R
import com.online.animall.databinding.ActivityMainBinding
import com.online.animall.databinding.FragmentAnimalCommBinding
import com.online.animall.signup.VerifyMobile
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var bottomNav: BottomNavigationView
    var selectedTab: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocaleHelper.getLocale(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNav = binding.bottomNav

        intent?.let {
            selectedTab = it.getStringExtra("SELECTED_TAB") // Default tab
            if(selectedTab == "2") {
                loadFragment(SellAnimalFragment())
                binding.bottomNav.selectedItemId = R.id.sell_animal
            } else loadFragment(HomeFragment())
        }
        setUpBottomNavItemSelect()


    }

    private fun setUpBottomNavItemSelect() {
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.buy_animal -> {
                    loadFragment(BuyAnimalFragment())
                    true
                }
                R.id.sell_animal -> {
                    loadFragment(SellAnimalFragment())
                    true
                }

                R.id.animal_communicate -> {
                    loadFragment(AnimalCommFragment())
                    true
                }

                else -> {
                    loadFragment(AnimalDoctorFragment())
                    true
                }
            }
        }

    }

    fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    override fun onStart() {
        super.onStart()
        /*Toast.makeText(this, "Current Locale: ${Locale.getDefault().language}", Toast.LENGTH_SHORT)
            .show()
        Toast.makeText(this, "onStart() called", Toast.LENGTH_SHORT).show()*/
        LocaleHelper.getLocale(this)
        updateBottomNavText()
    }

    private fun updateBottomNavText() {
        val menu = binding.bottomNav.menu
        menu.findItem(R.id.home).title =
            getString(R.string.home) // Update the string resource accordingly
        menu.findItem(R.id.buy_animal).title =
            getString(R.string.buy) // Update the string resource accordingly
        menu.findItem(R.id.sell_animal).title =
            getString(R.string.sell) // Update the string resource accordingly
        menu.findItem(R.id.animal_communicate).title =
            getString(R.string.animal_conversation) // Update the string resource accordingly
        menu.findItem(R.id.animal_doctor).title =
            getString(R.string.animal_doctor) // Update the string resource accordingly
    }

}