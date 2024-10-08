package com.online.animall.filters

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.online.animall.R
import com.online.animall.adapter.RadioSelectAdapter

class FilterSearchAnimalDialogFragment : BottomSheetDialogFragment() {

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            com.online.animall.R.layout.filter_search_animal, container,
            false
        )
        val typeAdapter = RadioSelectAdapter(listOf("Cow", "Buffalo"), requireContext())
        val typeRecView = view.findViewById<RecyclerView>(R.id.type_rec_view)
        typeRecView.layoutManager = GridLayoutManager(requireContext(), 2)
        typeRecView.adapter = typeAdapter

        val breedAdapter = RadioSelectAdapter(listOf("Cow", "Buffalo"), requireContext())
        val breedRecView = view.findViewById<RecyclerView>(R.id.breed_rec_view)
        breedRecView.layoutManager = GridLayoutManager(requireContext(), 2)
        breedRecView.adapter = breedAdapter

        val lactationAdapter = RadioSelectAdapter(listOf("Cow", "Buffalo"), requireContext())
        val lactationRecView = view.findViewById<RecyclerView>(R.id.lactation_rec_view)
        lactationRecView.layoutManager = GridLayoutManager(requireContext(), 2)
        lactationRecView.adapter = lactationAdapter

        val milkAdapter = RadioSelectAdapter(listOf("Cow", "Buffalo"), requireContext())
        val milkRecView = view.findViewById<RecyclerView>(R.id.milk_rec_view)
        milkRecView.layoutManager = GridLayoutManager(requireContext(), 2)
        milkRecView.adapter = milkAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            val layout = view.findViewById<CoordinatorLayout>(com.online.animall.R.id.bottom_sheet);
            layout.minimumHeight= (Resources.getSystem().displayMetrics.heightPixels)
        } catch(e: Exception) {
            Toast.makeText(requireContext(), "wrong", Toast.LENGTH_SHORT).show()

        }
    }



    companion object {
        fun newInstance(): FilterSearchAnimalDialogFragment {
            return FilterSearchAnimalDialogFragment()
        }
    }
}