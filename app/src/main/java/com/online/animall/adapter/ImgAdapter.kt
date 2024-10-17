package com.online.animall.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.online.animall.R

class ImgAdapter(var imgList: MutableList<Uri>): RecyclerView.Adapter<ImgAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<ImageView>(R.id.img)
        val delete = itemView.findViewById<ImageView>(R.id.ic_close)
        val imgPath = itemView.findViewById<TextView>(R.id.img_path)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fetch_photo_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = imgList.size

    override fun onBindViewHolder(holder: ImgAdapter.ViewHolder, position: Int) {
        val img = imgList[position]
        holder.img.setImageURI(img)
        holder.imgPath.text = img.toString().substringAfterLast('/')
        holder.delete.setOnClickListener {
            imgList.removeAt(position)
            notifyDataSetChanged()
        }
    }

    fun updateList(list: MutableList<Uri>) {
        imgList = list
        notifyDataSetChanged()
    }

}