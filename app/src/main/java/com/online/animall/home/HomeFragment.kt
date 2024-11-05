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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.online.animall.R
import com.online.animall.activity.ProfileActivity
import com.online.animall.adapter.HomeBuyAnimalAdapter
import com.online.animall.data.local.UserPreferences
import com.online.animall.databinding.FragmentHomeBinding
import com.online.animall.presentation.viewmodel.AnimalViewModel
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class HomeFragment : Fragment() {
    
    private lateinit var binding: FragmentHomeBinding
    private lateinit var animalImgList: MutableList<String>
    private lateinit var milkCapacityList: MutableList<String>
    private val viewModel: AnimalViewModel by viewModels()
    private lateinit var userPrefs: UserPreferences
    private lateinit var adapter: HomeBuyAnimalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false))
        binding.profileBtn.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileActivity::class.java))
        }
        userPrefs = UserPreferences(requireContext())

        val activity = (activity as MainActivity)
        fetchData(activity.refreshLayout)

        activity.refreshLayout.setOnRefreshListener {
            fetchData(activity.refreshLayout)
        }
        binding.buyAnimalBtn.setOnClickListener {
            activity.loadFragment(BuyAnimalFragment())
            activity.bottomNav.selectedItemId = R.id.buy_animal
        }
        binding.viewAll.setOnClickListener {
            activity.loadFragment(BuyAnimalFragment())
            activity.bottomNav.selectedItemId = R.id.buy_animal
        }
        binding.sellAnimalBtn.setOnClickListener {
            activity.loadFragment(SellAnimalFragment())
            activity.bottomNav.selectedItemId = R.id.sell_animal
        }
        binding.animalDoctorBtn.setOnClickListener {
            activity.loadFragment(AnimalDoctorFragment())
            activity.bottomNav.selectedItemId = R.id.animal_doctor
        }
        binding.animalCommBtn.setOnClickListener {
            activity.loadFragment(AnimalCommFragment())
            activity.bottomNav.selectedItemId = R.id.animal_communicate
        }
        return binding.root
    }


    private fun fetchData(refreshLayout: SwipeRefreshLayout) {
        milkCapacityList = mutableListOf()
        animalImgList = mutableListOf()
        try {
            viewModel.getYourAnimal(userPrefs.getToken()!!, object: AnimalViewModel.ResponseCallback {
                override fun onSuccess(response: Response<ResponseBody>) {
                    refreshLayout.isRefreshing = false
                    val data = JSONObject(response.body()!!.string())
                    val list = data.getJSONArray("data")
                    for(i in 0 until list.length()) {
                        if(i == 3) break
                        val item = list.getJSONObject(i)
                        val files = item.getJSONArray("files")
                        for(j in 0 until files.length()) {
                            animalImgList.add(files.getJSONObject(j)["path"].toString())
                        }
                       if(item.has("milkCapacity"))
                        milkCapacityList.add(item["milkCapacity"].toString())
                    }
                    Log.i("Animal Response : ", animalImgList.toString())
                    adapter = HomeBuyAnimalAdapter(animalImgList, milkCapacityList, requireContext())
                    binding.recView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.recView.adapter = adapter
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