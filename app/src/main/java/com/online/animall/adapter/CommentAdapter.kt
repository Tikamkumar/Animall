package com.online.animall.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.online.animall.R
import com.online.animall.data.local.UserPreferences
import com.online.animall.data.model.CommentModel
import com.online.animall.presentation.viewmodel.PostViewModel
import com.online.animall.utils.SnackbarUtil
import okhttp3.ResponseBody
import retrofit2.Response

class CommentAdapter(private var commentList: MutableList<CommentModel>, private var postId: String, private var viewModel: PostViewModel): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    var context: Context? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userName = itemView.findViewById<TextView>(R.id.user_name)
//        val userPic = itemView.findViewById<ImageView>(R.id.user_pic)
        val comment = itemView.findViewById<TextView>(R.id.comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = commentList.size

    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        val comment = commentList[position]

        holder.itemView.setOnLongClickListener {
            if(comment.userId == UserPreferences(context!!).getUserId()!!) {
                deleteDialog(comment.id)
            }
            true
        }
        /*if(comment.userPic != "") {
            Picasso.get()
                .load("http://192.168.1.7:5001/uploads/${post.userPic}")
                *//*.placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)*//*
                .into(holder.userPic);
        }*/
        holder.userName.text = comment.userId
        holder.comment.text = comment.comment
    }

    private fun deleteDialog(id: String) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Delete Comment ?")
            .setPositiveButton("yes") { dialog, which ->
                deleteFunc(id)
            }
                .setNegativeButton("no") { dialog, _ ->
                dialog.dismiss()
            }
            .setIcon(android.R.drawable.ic_delete)
            .show()
    }

    private fun deleteFunc(commentId: String) {
       viewModel.deleteComment(UserPreferences(context!!).getToken()!!, commentId, postId, object: PostViewModel.ResponseCallback {
           override fun onSuccess(response: Response<ResponseBody>) {
               Log.i("Response: ", response.body()!!.string())
           }
           override fun onError(error: String) {
               Log.e("Error: ", error)
           }
       })
    }

    fun updateList(list: MutableList<CommentModel>) {
        commentList = list
    }

    fun getCommentList(): MutableList<CommentModel> {
        return commentList
    }
}