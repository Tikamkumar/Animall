package com.online.animall.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.online.animall.adapter.CommentAdapter
import com.online.animall.data.local.UserPreferences
import com.online.animall.data.model.CommentModel
import com.online.animall.databinding.CommentLayoutBinding
import com.online.animall.presentation.viewmodel.PostViewModel
import com.online.animall.utils.SnackbarUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class CommentDialog(private val postId: String, private val context: Context, var commentList: MutableList<CommentModel>) : BottomSheetDialogFragment() {

    private lateinit var binding: CommentLayoutBinding
    private val viewModel: PostViewModel by viewModels()
    private lateinit var userPrefs: UserPreferences
    private lateinit var commentAdapter: CommentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CommentLayoutBinding.bind(inflater.inflate(
            com.online.animall.R.layout.comment_layout,
            container, false
        ))
        setUpRecyclerView()
        userPrefs = UserPreferences(context)
        binding.icClose.setOnClickListener {
            dialog!!.dismiss()
        }
        binding.postComment.setOnClickListener {
            postComment()
        }
        binding.commentText.requestFocus()
        return binding.root
    }

    private fun setUpRecyclerView() {
        commentAdapter = CommentAdapter(commentList, postId, viewModel)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = commentAdapter
    }

    private fun postComment() {
        val comment = binding.commentText.text.toString()
        if(comment.isEmpty()) {
            SnackbarUtil.error(binding.root, "Write Something..")
            return
        }
        showProgress(true)
        try {
            val body = comment.toRequestBody("text/plain".toMediaTypeOrNull())
            viewModel.postComment(userPrefs.getToken()!!, postId, body, object: PostViewModel.ResponseCallback {
                override fun onSuccess(response: Response<ResponseBody>) {
                    showProgress(false)
                    binding.commentText.text.clear()
                    val data = JSONObject(response.body()!!.string()).getJSONArray("data")
                    commentList.clear()
                    for(i in 0 until data.length()) {
                        commentList.add(CommentModel(
                            data.getJSONObject(i)["userId"].toString(),
                            data.getJSONObject(i)["comment"].toString(),
                            data.getJSONObject(i)["_id"].toString()
                        ))
                    }
                    commentAdapter.updateList(commentList)
                }
                override fun onError(error: String) {
                    showProgress(false)
                    Log.e("Error: ", error)
                    SnackbarUtil.error(binding.root)
                }
            })
        } catch (exp: Exception) {
            Log.e("Error: ", exp.message.toString())
            showProgress(false)
            SnackbarUtil.error(binding.root)
        }
    }

    private fun showProgress(show: Boolean) {
        if(show) {
            binding.postComment.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.postComment.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { dialog ->
            val behavior = BottomSheetBehavior.from(dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(commentAdapter.getCommentList().isNotEmpty()) {
            commentList = commentAdapter.getCommentList()
        }
    }
}
