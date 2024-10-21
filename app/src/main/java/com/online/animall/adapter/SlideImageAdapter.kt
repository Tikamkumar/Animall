package com.online.animall.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso
import java.util.Objects

class SlideImageAdapter(
    var context: Context, // Array of images
    var images: List<String>
) : PagerAdapter() {
    var mLayoutInflater: LayoutInflater
    var currentPos = 0

    init {
        mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === (`object` as RelativeLayout)
    }

    @SuppressLint("MissingInflatedId")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // inflating the item.xml
        val itemView: View = mLayoutInflater.inflate(com.online.animall.R.layout.single_img_layout, container, false)

        val imageView = itemView.findViewById<ImageView>(com.online.animall.R.id.animal_img)
        val previous = itemView.findViewById<ImageView>(com.online.animall.R.id.previous)
        val next = itemView.findViewById<ImageView>(com.online.animall.R.id.next)
//        imageView.setImageResource(images[position])
        loadImage(images[position], imageView)

        previous.setOnClickListener {
            if(currentPos > 0) {
                currentPos--
                loadImage(images[currentPos], imageView)
            }
        }

        next.setOnClickListener {
            if(currentPos < images.size - 1) {
                currentPos++
                loadImage(images[currentPos], imageView)
            }
        }
        // Adding the View
        Objects.requireNonNull(container).addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

    private fun loadImage(img: String, view: ImageView) {
        Picasso.get()
            .load("http://192.168.1.7:5001/uploads/$img")
            /*.placeholder(R.drawable.user_placeholder)
            .error(R.drawable.user_placeholder_error)*/
            .into(view);
    }
}
