package com.online.animall.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.online.animall.R
import com.online.animall.data.model.BuyAnimalModel

class YourAnimalAdapter(private val context: Context, private val list: MutableList<BuyAnimalModel>): RecyclerView.Adapter<YourAnimalAdapter.ViewHolder>() {

    private var mViewPagerAdapter: SlideImageAdapter? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val time: TextView = itemView.findViewById(R.id.time)
        val viewPager: ViewPager = itemView.findViewById(R.id.view_pager)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.buy_animal_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.milkCapacity
        holder.time.text = item.createdAt
        mViewPagerAdapter = SlideImageAdapter(context, item.images)
        holder.viewPager.adapter = mViewPagerAdapter
    }
}