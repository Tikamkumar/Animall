package com.online.animall.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.online.animall.R
import com.online.animall.activity.NewPostActivity
import com.online.animall.adapter.PostAdapter
import com.online.animall.data.local.UserPreferences
import com.online.animall.data.model.CommentModel
import com.online.animall.data.model.PostModel
import com.online.animall.data.model.likeModel
import com.online.animall.databinding.FragmentAnimalCommBinding
import com.online.animall.dialog.CommentDialog
import com.online.animall.presentation.viewmodel.AnimalViewModel
import com.online.animall.presentation.viewmodel.PostViewModel
import com.online.animall.utils.SnackbarUtil
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class AnimalCommFragment : Fragment() {

    private lateinit var binding: FragmentAnimalCommBinding
    private val viewModel: PostViewModel by viewModels()
    private lateinit var userPrefs: UserPreferences
    private var postList: MutableList<PostModel> = mutableListOf()
    private lateinit var postAdapter: PostAdapter
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnimalCommBinding.bind(inflater.inflate(R.layout.fragment_animal_comm, container, false))

        userPrefs = UserPreferences(requireContext())

        mainActivity = (activity as MainActivity)
//        setUpRecyclerView()

        mainActivity.refreshLayout.setOnRefreshListener {
            postList.clear()
            fetchData()
        }

        binding.createPost.setOnClickListener {
            startActivity(Intent(requireContext(), NewPostActivity::class.java))
        }

        try {
            viewModel._postResponse.observe(requireActivity()) { response ->
                mainActivity.refreshLayout.isRefreshing = false

                if(response!!.isSuccessful) {
                    val postArray = JSONObject(response.body()!!.string()).getJSONArray("allposts")
                    Log.i("post : ", postArray.length().toString())
                    /*if(postArray.length() == 0) return*/
                    for(item in 0 until postArray.length()) {
                        val postData = postArray.getJSONObject(item)

                        var likes: MutableList<String> = mutableListOf()
                        if(postData.getJSONArray("likes").length() > 0) {
                            for(index in 0 until postData.getJSONArray("likes").length()) {
                                likes.add(
                                    postData.getJSONArray("likes").getJSONObject(index)["userId"].toString()
                                )
                            }
                        }
                        val comments: MutableList<CommentModel> = mutableListOf()
                        if(postData.getJSONArray("comments").length() > 0) {
                            for(i in 0 until postData.getJSONArray("comments").length()) {
                                val comment = CommentModel(
                                    postData.getJSONArray("comments").getJSONObject(i)["userId"].toString(),
                                    postData.getJSONArray("comments").getJSONObject(i)["comment"].toString(),
                                    postData.getJSONArray("comments").getJSONObject(i)["_id"].toString(),
                                )
                                comments.add(comment)
                            }
                        }

                        val post = PostModel(
                            postData["_id"].toString(),
                            postData.getJSONObject("userId")["_id"].toString(),
                            postData.getJSONObject("userId")["name"].toString(),
                            postData.getJSONObject("userId")["photo"].toString(),
                            postData["text"].toString(),
                            postData["content"].toString(),
                            likes,
                            comments,
                            postData["createdAt"].toString(),
                        )
                        Log.i("postList : ", item.toString())
                        postList.add(post)
                    }

                    setUpRecyclerView()
                } else {
                    SnackbarUtil.error(binding.main)
                }
            }
        } catch (exp: Exception) {
            mainActivity.refreshLayout.isRefreshing = false
        }

        fetchData()

        return binding.root
    }

    private fun fetchData() {
        try {
            viewModel.getAllPost(userPrefs.getToken()!!)
        } catch (exp: Exception) {
            mainActivity.refreshLayout.isRefreshing = false
        }
    }

    private fun setUpRecyclerView() {
        postAdapter = PostAdapter(postList, viewModel, userPrefs.getToken()!!, this)
        binding.recView.layoutManager = LinearLayoutManager(requireContext())
        binding.recView.adapter = postAdapter
    }

    override fun onResume() {
        super.onResume()

    }
}