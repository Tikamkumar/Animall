package com.online.animall.home

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
import com.online.animall.adapter.DoctorListAdapter
import com.online.animall.data.local.UserPreferences
import com.online.animall.data.model.DoctorModel
import com.online.animall.databinding.FragmentAnimalDoctorBinding
import com.online.animall.presentation.viewmodel.DoctorViewModel
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class AnimalDoctorFragment : Fragment() {

    private lateinit var binding: FragmentAnimalDoctorBinding
    private var selectedTab = 0
    private lateinit var mainActivity: MainActivity
    private val viewModel: DoctorViewModel by viewModels()
    private lateinit var userPrefs: UserPreferences
    private lateinit var doctorList: MutableList<DoctorModel>
    private lateinit var adapter: DoctorListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnimalDoctorBinding.bind(inflater.inflate(R.layout.fragment_animal_doctor, container, false))

        userPrefs = UserPreferences(requireContext())
        mainActivity = (activity as MainActivity)

        fetchNearDoctor(mainActivity.refreshLayout)

        binding.nearDoctor.setOnClickListener {
            selectedTab = 0
            switchTab()
            fetchNearDoctor(mainActivity.refreshLayout)
        }

        binding.experienceDoctor.setOnClickListener {
            selectedTab = 1
            switchTab()
            fetchExpDoctor(mainActivity.refreshLayout)
        }

        return binding.root
    }

    private fun fetchExpDoctor(refreshLayout: SwipeRefreshLayout) {
        doctorList = mutableListOf()
        try {
            viewModel.getExpDoctor(userPrefs.getToken()!!, object: DoctorViewModel.ResponseCallBack {
                override fun onSuccess(response: Response<ResponseBody>) {
                    refreshLayout.isRefreshing = false
                    val data = JSONObject(response.body()!!.string()).getJSONArray("data")
                    for(i in 0 until data.length()) {
                        val item = data.getJSONObject(i)
                        doctorList.add(
                            DoctorModel(
                               item["image"].toString(),
                               item["name"].toString(),
                               item["profession"].toString(),
                               item["education"].toString(),
                               item["experience"].toString(),
                               item["address"].toString(),
                               item["whatsUpNo"].toString(),
                               item["phoneNo"].toString(),
                            )
                        )
                    }
                    adapter = DoctorListAdapter(doctorList, requireContext())
                    binding.recView.layoutManager = LinearLayoutManager(requireContext())
                    binding.recView.adapter = adapter

                }
                override fun onError(error: String) {
                    refreshLayout.isRefreshing = false
                    Log.e("Error : ", error)
                }
            })
        } catch(exp: Exception) {
            Log.e("Error: ", exp.toString())
            refreshLayout.isRefreshing = false
        }
    }

    private fun fetchNearDoctor(refreshLayout: SwipeRefreshLayout) {
        doctorList = mutableListOf()
        try {
            viewModel.getNearDoctor(userPrefs.getToken()!!, object: DoctorViewModel.ResponseCallBack {
                override fun onSuccess(response: Response<ResponseBody>) {
                    refreshLayout.isRefreshing = false
                    val data = JSONObject(response.body()!!.string()).getJSONArray("data")
                    for(i in 0 until data.length()) {
                        val item = data.getJSONObject(i)
                        doctorList.add(
                            DoctorModel(
                                item["image"].toString(),
                                item["name"].toString(),
                                item["profession"].toString(),
                                item["education"].toString(),
                                item["experience"].toString(),
                                item["address"].toString(),
                                item["whatsUpNo"].toString(),
                                item["phoneNo"].toString(),
                            )
                        )
                    }
                    adapter = DoctorListAdapter(doctorList, requireContext())
                    binding.recView.layoutManager = LinearLayoutManager(requireContext())
                    binding.recView.adapter = adapter

                }
                override fun onError(error: String) {
                    refreshLayout.isRefreshing = false
                    Log.e("Error : ", error)
                }
            })
        } catch(exp: Exception) {
            Log.e("Error: ", exp.toString())
            refreshLayout.isRefreshing = false
        }
    }

    private fun switchTab() {
        when(selectedTab) {
            0 -> {
                binding.nearDoctor.setTextColor(resources.getColor(R.color.white, null))
                binding.experienceDoctor.setTextColor(resources.getColor(R.color.black, null))
                binding.nearDoctor.setBackgroundColor(resources.getColor(R.color.navy, null))
                binding.experienceDoctor.setBackgroundColor(resources.getColor(R.color.white, null))
            }
            1 -> {
                binding.nearDoctor.setTextColor(resources.getColor(R.color.black, null))
                binding.experienceDoctor.setTextColor(resources.getColor(R.color.white, null))
                binding.nearDoctor.setBackgroundColor(resources.getColor(R.color.white, null))
                binding.experienceDoctor.setBackgroundColor(resources.getColor(R.color.navy, null))
            }
        }
    }

}