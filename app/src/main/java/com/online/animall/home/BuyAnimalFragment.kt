package com.online.animall.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.online.animall.R
import com.online.animall.adapter.SellAnimalAdapter
import com.online.animall.databinding.FragmentBuyAnimalBinding
import com.online.animall.fragments.BuyAllAnimalFragment
import com.online.animall.fragments.BuyPrimeAnimalFragment
import com.online.animall.fragments.SellAnimalFormFragment
import com.online.animall.fragments.YourSellAnimalFragment

class BuyAnimalFragment : Fragment() {

    private lateinit var binding: FragmentBuyAnimalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBuyAnimalBinding.bind(inflater.inflate(R.layout.fragment_buy_animal, container, false))
        setUpTabAndViewPager()
        return binding.root
    }

    private fun setUpTabAndViewPager() {
        val tabAdapter = SellAnimalAdapter(requireActivity())
        tabAdapter.addFragment(BuyAllAnimalFragment(), getString(R.string.all_animal))
        tabAdapter.addFragment(BuyPrimeAnimalFragment(), getString(R.string.prime_animal))
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
//        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }


}