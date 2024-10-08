package com.online.animall.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.online.animall.R
import com.online.animall.adapter.SellAnimalAdapter
import com.online.animall.databinding.ActivityAnimalForYouBinding
import com.online.animall.databinding.ActivityWalletBinding
import com.online.animall.fragments.ChoiceAnimalFragment
import com.online.animall.fragments.SelectedAnimalFragment
import com.online.animall.fragments.SellAnimalFormFragment
import com.online.animall.fragments.YourSellAnimalFragment

class AnimalForYouActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnimalForYouBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAnimalForYouBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.animal_for_you)

        setUpTabAndViewPager()
    }

    private fun setUpTabAndViewPager() {
        val tabAdapter = SellAnimalAdapter(this)
        tabAdapter.addFragment(ChoiceAnimalFragment(), getString(R.string.according_choice))
        tabAdapter.addFragment(SelectedAnimalFragment(), getString(R.string.selected_animal))
        binding.viewPager.setAdapter(tabAdapter)
        // Setup TabLayout with ViewPager2
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(tabAdapter.getFragmentTitle(0)))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(tabAdapter.getFragmentTitle(1)))

        // Link TabLayout with ViewPager2
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.getTabAt(position)?.select()
            }
        })

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
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

}