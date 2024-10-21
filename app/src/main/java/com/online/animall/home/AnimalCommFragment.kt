package com.online.animall.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.online.animall.R
import com.online.animall.activity.NewPostActivity
import com.online.animall.adapter.PostAdapter
import com.online.animall.data.local.UserPreferences
import com.online.animall.data.model.CommentModel
import com.online.animall.data.model.PostModel
import com.online.animall.databinding.FragmentAnimalCommBinding
import com.online.animall.presentation.viewmodel.AnimalViewModel
import com.online.animall.utils.SnackbarUtil
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class AnimalCommFragment : Fragment() {

    private lateinit var binding: FragmentAnimalCommBinding
    private val viewModel: AnimalViewModel by viewModels()
    private lateinit var userPrefs: UserPreferences
    private var postList: MutableList<PostModel> = mutableListOf()
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnimalCommBinding.bind(inflater.inflate(R.layout.fragment_animal_comm, container, false))

        userPrefs = UserPreferences(requireContext())

        fetchData()
        binding.createPost.setOnClickListener {
            startActivity(Intent(requireContext(), NewPostActivity::class.java))
        }
        return binding.root
    }

    private fun fetchData() {
        try {
            viewModel.getAllPost(userPrefs.getToken()!!, object: AnimalViewModel.ResponseCallback {
                override fun onSuccess(response: Response<ResponseBody>) {
                    Log.i("Response: ", response.body()!!.string())
                    val allPost = JSONObject(response.body()!!.string()).getJSONArray("allposts")
                    if(allPost.length() == 0) return
                    for(item in 0 until allPost.length()) {
                       val postData = allPost.getJSONObject(item)
                        var likes: MutableList<String> = mutableListOf()
                       if(postData.getJSONArray("likes").length() > 0) {
                           for(index in 0 until postData.getJSONArray("likes").length()) {
                               likes.add(postData.getJSONArray("likes")[index].toString())
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
                           postData.getJSONObject("_id").toString(),
                           postData.getJSONObject("userId").toString(),
                           postData.getJSONObject("text").toString(),
                           postData.getJSONObject("content").toString(),
                           likes,
                           comments,
                           postData.getJSONObject("createdAt").toString(),
                       )
                        postList.add(post)
                    }
                    setUpRecyclerView()
                }

                override fun onError(error: String) {
                    SnackbarUtil.error(binding.root, error)
                }

            })
        } catch (exp: Exception) {
            SnackbarUtil.error(binding.root, exp.message.toString())
        }
    }

    private fun setUpRecyclerView() {
        postAdapter = PostAdapter(postList)
        binding.recView.layoutManager = LinearLayoutManager(requireContext())
        binding.recView.adapter = postAdapter
    }
}