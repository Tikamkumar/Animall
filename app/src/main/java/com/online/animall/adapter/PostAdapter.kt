package com.online.animall.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.online.animall.R
import com.online.animall.data.local.UserPreferences
import com.online.animall.data.model.CommentModel
import com.online.animall.data.model.PostModel
import com.online.animall.databinding.PostLayoutBinding
import com.online.animall.dialog.CommentDialog
import com.online.animall.presentation.viewmodel.PostViewModel
import com.online.animall.utils.ConstUtil
import com.squareup.picasso.Picasso
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class PostAdapter(private var posts: MutableList<PostModel>, private val viewModel: PostViewModel, private val token: String, private val context: Fragment): RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private val userPrefs = UserPreferences(context.requireContext())
    private  var alreadyLike: Boolean = false

    inner class ViewHolder(binding: PostLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        val postText = binding.text
        val imgTitle = binding.imgTitle
        val img = binding.postImg
        val textPostLayout = binding.onlyTextLayout
        val ImgPostLayout = binding.textImgLayout
        val like = binding.like
        val likeBtn = binding.likeBtn
        val commentBtn = binding.commentBtn
        val username = binding.profileName
        val userPic = binding.icProfile
        val time = binding.time
        val addComment = binding.addComment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        val binding = PostLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
       var post = posts[position]
        if(post.content == "") {
            holder.textPostLayout.visibility = View.VISIBLE
            holder.ImgPostLayout.visibility = View.GONE
            holder.postText.text = post.text
        } else {
            holder.ImgPostLayout.visibility = View.VISIBLE
            holder.textPostLayout.visibility = View.GONE
            if(post.text == "") holder.imgTitle.visibility = View.GONE
            holder.imgTitle.text = post.text
            Picasso.get()
                .load("http://192.168.1.9:5001/uploads/${post.content}")
                /*.placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)*/
                .into(holder.img);
        }
        holder.like.text = post.likes.size.toString() + " likes"

        alreadyLike = post.likes.contains(userPrefs.getUserId()!!)
        if(alreadyLike)
            holder.likeBtn.setImageResource(R.drawable.ic_like)

        holder.username.text = post.userName
        holder.time.text = getTime(post.createdAt)
        holder.commentBtn.setOnClickListener {
            commentSheet(post.id, post.comments)
        }
        holder.addComment.setOnClickListener {
            commentSheet(post.id, post.comments)
        }
        if(post.userPic != "") {
            Picasso.get()
                .load("http://192.168.1.9:5001/uploads/${post.userPic}")
                /*.placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)*/
                .into(holder.img);
        }
        holder.likeBtn.setOnClickListener {
            likeUnlikeFunc(post.id, holder.likeBtn, holder.like)
        }
    }

    fun updateList(list: MutableList<PostModel>) {
        posts = list
    }

    private fun commentSheet(postId: String, comments: List<CommentModel>) {
        val bottomSheet = CommentDialog(postId, context.requireContext(), comments.toMutableList())
        bottomSheet.show(
            context.parentFragmentManager,
            "ModalBottomSheet"
        )
    }

    private fun likeUnlikeFunc(id: String, icon: ImageView, like: TextView) {
        try {
            viewModel.postLike(token, id, object: PostViewModel.ResponseCallback {
                override fun onSuccess(response: Response<ResponseBody>) {
                    val response = JSONObject(response.body()!!.string())
                    when(response["successMsg"].toString()) {
                        "like" -> icon.setImageResource(R.drawable.ic_like)
                        "dislike" -> icon.setImageResource(R.drawable.ic_dislike)
                    }
                    like.text = response["totalLikes"].toString() + " likes"
                }
                override fun onError(error: String) {
                    Log.e("Error", error)
                }
            })
        } catch (exp: Exception) {
            Log.e("Error", exp.message.toString())
        }
    }

    override fun getItemCount(): Int = posts.size

    private fun getTime(timestamp: String): String {
        val timestamp = "2024-10-22T08:24:18.040Z"

        // Parse the timestamp to a Date object
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC") // Set time zone to UTC
        }.parse(timestamp)

        // Format the date to "10 Oct 2024"
        val outputFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val formattedDate = outputFormatter.format(date)

        return formattedDate
    }
}