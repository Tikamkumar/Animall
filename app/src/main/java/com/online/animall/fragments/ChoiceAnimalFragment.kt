package com.online.animall.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.online.animall.R
import com.online.animall.databinding.FragmentChoiceAnimalBinding
import com.online.animall.filters.FilterSearchAnimalDialogFragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChoiceAnimalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChoiceAnimalFragment : Fragment() {

    private lateinit var binding: FragmentChoiceAnimalBinding

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }*/

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChoiceAnimalBinding.bind(inflater.inflate(R.layout.fragment_choice_animal, container, false))
        binding.searchView.setOnClickListener {
            val addPhotoBottomDialogFragment =
                FilterSearchAnimalDialogFragment.newInstance()
            addPhotoBottomDialogFragment.show(
                requireActivity().supportFragmentManager,
                "add_photo_dialog_fragment"
            )
            /*val dialog = BottomSheetDialog(requireContext())
            val view = layoutInflater.inflate(com.online.animall.R.layout.filter_search_animal, null)

            *//* val btnClose = view.findViewById<ImageView>(com.online.animall.R.id.ic_close)
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
            }*//*
            dialog.setContentView(view)
            *//*dialog.setOnShowListener {
                // Set full-screen behavior
                val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                val behavior = BottomSheetBehavior.from(bottomSheet!!)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }*//**//*
            val bottomSheet = dialog.findViewById<View>(com.online.animall.R.id.bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.peekHeight = 0 // Optional: Adjust peek height if necessary
            behavior.state = BottomSheetBehavior.STATE_EXPANDED*//*
            dialog.show()*/
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChoiceAnimalFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChoiceAnimalFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}