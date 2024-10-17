package com.online.animall.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.online.animall.R
import com.online.animall.data.model.WhichAnimalModel

class SingleChoiceAdapter(private val context: Context, private val list: MutableList<String>, val dialog: Dialog, val onItemSelected: (Int) -> Unit): RecyclerView.Adapter<SingleChoiceAdapter.ViewHolder>() {

//    var selectedPos: Int = selected

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_choice_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.animal.text = list[position]

        holder.itemView.setOnClickListener {
            onItemSelected(position)
            dialog.dismiss()
        }
        /*holder.radioButton.isChecked = position == selectedPos
//        holder.radioButton.isSelected = list[position].isSelected
        holder.itemView.setOnClickListener {
            val prevPosition = selectedPos
            selectedPos = position
            notifyItemChanged(prevPosition)
            notifyItemChanged(selectedPos)
            dialog.cancel()
        }

        holder.radioButton.setOnClickListener {
            val prevPosition = selectedPos
            selectedPos = position
            notifyItemChanged(prevPosition)
            notifyItemChanged(selectedPos)
            dialog.cancel()
        }*/
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val animal = itemView.findViewById<TextView>(R.id.animal)!!
        var radioButton = itemView.findViewById<RadioButton>(R.id.choice_btn)
    }

}