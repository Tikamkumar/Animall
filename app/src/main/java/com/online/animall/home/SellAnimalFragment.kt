package com.online.animall.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.online.animall.R
import com.online.animall.adapter.SellAnimalAdapter
import com.online.animall.databinding.FragmentSellAnimalBinding
import com.online.animall.fragments.SellAnimalFormFragment
import com.online.animall.fragments.YourSellAnimalFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SellAnimalFragment : Fragment() {

    private lateinit var binding: FragmentSellAnimalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSellAnimalBinding.bind(inflater.inflate(R.layout.fragment_sell_animal, container, false))

        setUpTabAndViewPager()

        val activity = (activity as MainActivity)
        if(activity.selectedTab == "2") {
            binding.viewPager.currentItem = 1
            binding.tabLayout.getTabAt(1)?.select()
        }
        return binding.root
    }

    private fun setUpTabAndViewPager() {
        val tabAdapter = SellAnimalAdapter(requireActivity())
        tabAdapter.addFragment(SellAnimalFormFragment(), getString(R.string.sell_animal))
        tabAdapter.addFragment(YourSellAnimalFragment(), getString(R.string.your_selling_animal))
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


        /*binding.viewPager.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event!!.action === MotionEvent.ACTION_DOWN &&
                    v is ViewGroup
                ) {
                    v.requestDisallowInterceptTouchEvent(true)
                }
                return false
            }
        })*/

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SellAnimalFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SellAnimalFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}