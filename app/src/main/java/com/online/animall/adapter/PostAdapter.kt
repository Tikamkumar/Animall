package com.online.animall.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.online.animall.data.model.PostModel
import com.online.animall.databinding.PostLayoutBinding
import com.squareup.picasso.Picasso

class PostAdapter(private val posts: MutableList<PostModel>): RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    inner class ViewHolder(binding: PostLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        val postText = binding.text
        val imgTitle = binding.imgTitle
        val img = binding.postImg
        val textPostLayout = binding.onlyTextLayout
        val ImgPostLayout = binding.textImgLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        val binding = PostLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
       val post = posts[position]
        if(post.text != "") {
            holder.textPostLayout.visibility = View.VISIBLE
            holder.postText.text = post.text
        } else {
            holder.ImgPostLayout.visibility = View.VISIBLE
            holder.imgTitle.text = post.text
            Picasso.get()
                .load("http://192.168.1.8:5001/uploads/${post.content}")
                /*.placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)*/
                .into(holder.img);
        }
    }

    override fun getItemCount(): Int = posts.size
}