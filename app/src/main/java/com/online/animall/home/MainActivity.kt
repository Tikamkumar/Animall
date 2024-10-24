package com.online.animall.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.online.animall.LocaleHelper
import com.online.animall.NetworkChecker
import com.online.animall.R
import com.online.animall.databinding.ActivityMainBinding
import com.online.animall.databinding.FragmentAnimalCommBinding
import com.online.animall.presentation.viewmodel.AnimalViewModel
import com.online.animall.signup.VerifyMobile
import java.util.Locale

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var bottomNav: BottomNavigationView
    var selectedTab: String? = null
    lateinit var refreshLayout: SwipeRefreshLayout
    lateinit var root: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocaleHelper.getLocale(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        internetChecker()

        root = binding.main
        bottomNav = binding.bottomNav
        refreshLayout = binding.refreshLayout

        binding.noInternet.tryAgainBtn.setOnClickListener {
            internetChecker()
        }

        intent?.let {
            selectedTab = it.getStringExtra("SELECTED_TAB") // Default tab
            if(selectedTab == "2") {
                loadFragment(SellAnimalFragment())
                binding.bottomNav.selectedItemId = R.id.sell_animal
            } else {
                loadFragment(HomeFragment())
            }
        }

        /*binding.refreshLayout.setOnRefreshListener {
            internetChecker()
        }*/

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
        refreshLayout.isRefreshing = true
        internetChecker()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    private fun internetChecker() {
        if(NetworkChecker.isInternetAvailable(this)) {
            binding.noInternet.noInternetLayout.visibility = View.GONE
        } else {
            binding.noInternet.noInternetLayout.visibility = View.VISIBLE
        }
    }
}