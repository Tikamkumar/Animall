package com.online.animall.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.online.animall.R
import com.online.animall.data.model.BuyAnimalModel
import com.online.animall.utils.ConstUtil
import com.squareup.picasso.Picasso

class HomeBuyAnimalAdapter(var imgList: MutableList<String>, private val capacityList: MutableList<String>, private var context: Context): RecyclerView.Adapter<HomeBuyAnimalAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val animalImg = itemView.findViewById<ImageView>(R.id.animal_img)
        val milkCapacity = itemView.findViewById<TextView>(R.id.milk_capacity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeBuyAnimalAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_buy_animal_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = imgList.size

    override fun onBindViewHolder(holder: HomeBuyAnimalAdapter.ViewHolder, position: Int) {

            Picasso.get()
                .load("http://192.168.1.12:5001/uploads/${imgList[position]}")
                /*.placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)*/
                .into(holder.animalImg)
        if(capacityList.size > position)
            holder.milkCapacity.text = capacityList[position] + " L ${context.getString(R.string.milk_capacity)}"
        else
            holder.milkCapacity.text = ""
    }

}