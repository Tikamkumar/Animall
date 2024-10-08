package com.online.animall.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.online.animall.R
import com.online.animall.databinding.FragmentSellAnimalFormBinding

class SellAnimalFormFragment : Fragment() {

    private lateinit var binding: FragmentSellAnimalFormBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSellAnimalFormBinding.bind(inflater.inflate(R.layout.fragment_sell_animal_form, container, false))
        binding.showHideMoreInfo.setOnClickListener {
            showHideInfoLayout()
        }
        return binding.root
    }

    private fun showHideInfoLayout() {
        if(binding.fillMoreInfoLayout.visibility == View.VISIBLE) {
            binding.fillMoreInfoLayout.visibility = View.GONE
            binding.root.post(Runnable {  binding.root.smoothScrollTo(0, binding.root.bottom) })
        } else {
            binding.fillMoreInfoLayout.visibility = View.VISIBLE
            binding.root.post(Runnable { binding.root.smoothScrollTo(0, binding.root.bottom) })
        }
    }
}