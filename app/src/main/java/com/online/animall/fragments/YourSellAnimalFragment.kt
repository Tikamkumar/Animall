package com.online.animall.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.online.animall.R
import com.online.animall.adapter.SlideImageAdapter
import com.online.animall.adapter.YourAnimalAdapter
import com.online.animall.data.local.UserPreferences
import com.online.animall.data.model.BuyAnimalModel
import com.online.animall.databinding.FragmentYourSellAnimalBinding
import com.online.animall.home.MainActivity
import com.online.animall.presentation.viewmodel.AnimalViewModel
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class YourSellAnimalFragment : Fragment() {

    private lateinit var itemList: MutableList<BuyAnimalModel>
    private lateinit var imgList: MutableList<String>
    private lateinit var adapter: YourAnimalAdapter
    private lateinit var binding: FragmentYourSellAnimalBinding
    private lateinit var userPrefs: UserPreferences
    private val viewModel: AnimalViewModel by viewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentYourSellAnimalBinding.bind(inflater.inflate(com.online.animall.R.layout.fragment_your_sell_animal, container, false))
        userPrefs = UserPreferences(requireContext())

        val activity = (activity as MainActivity)
        fetchData(activity.refreshLayout)

        activity.refreshLayout.setOnRefreshListener {
            fetchData(activity.refreshLayout)
        }

        return binding.root
    }

    private fun fetchData(refreshLayout: SwipeRefreshLayout) {
        itemList = mutableListOf()
        imgList = mutableListOf()
        try {
            viewModel.getYourAnimal(userPrefs.getToken()!!, object: AnimalViewModel.ResponseCallback {
                override fun onSuccess(response: Response<ResponseBody>) {
                    refreshLayout.isRefreshing = false
                    val data = JSONObject(response.body()!!.string())
                    val list = data.getJSONArray("data")
                    for(i in 0 until list.length()) {
                        val item = list.getJSONObject(i)
                        val files = item.getJSONArray("files")
                        for(j in 0 until files.length()) {
                            imgList.add(files.getJSONObject(j)["path"].toString())
                        }
                        itemList.add(
                            BuyAnimalModel(
                                item["_id"].toString(),
                                item["userId"].toString(),
                                item.getJSONObject("animalId")["_id"].toString(),
                                item.getJSONObject("breedId")["_id"].toString(),
                                item["lactation"].toString(),
                                item["currentMilk"].toString(),
                                item["milkCapacity"].toString(),
                                item["price"].toString(),
                                item["isNegotiable"].toString(),
                                imgList,
                                item.getJSONObject("optionalData")["animalBaby"].toString(),
                                item.getJSONObject("optionalData")["pregnent"].toString(),
                                item.getJSONObject("optionalData")["calfGender"].toString(),
                                item.getJSONObject("optionalData")["info"].toString(),
                                item["createdAt"].toString()
                            )
                        )
                    }
                    adapter = YourAnimalAdapter(requireContext(), itemList)
                    binding.recView.layoutManager = LinearLayoutManager(requireContext())
                    binding.recView.adapter = adapter
                    Log.i("Item List : ", itemList.toString())
                    Log.i("Response Animal: ", response.body()!!.string())
                }

                override fun onError(error: String) {
                    refreshLayout.isRefreshing = false
                    Log.e("Response Error: ", error)
                }

            })
        } catch(exp: Exception) {
            Log.e("Error: ", exp.toString())
            refreshLayout.isRefreshing = false
        }
    }
}