package com.online.animall.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.online.animall.LocaleHelper
import com.online.animall.adapter.BuyAnimalAdapter
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
    private lateinit var adapter: BuyAnimalAdapter
    private lateinit var binding: FragmentYourSellAnimalBinding
    private lateinit var userPrefs: UserPreferences
    private lateinit var animal: String
    private lateinit var breed: String
    private var lactation: String? = null
    private var pregnent: String? = null
    private var calfGender: String? = null
    private var animalBaby: String? = null
    private var moreInfo: String? = null
    private var currentMilk: String? = null
    private var milkCapacity: String? = null
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

        binding.isPrime.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener {_, isChecked ->
            when(isChecked) {
                true -> {
                    fetchPrimeData(activity.refreshLayout)
                }
                false -> {
                    fetchData(activity.refreshLayout)
                }
            }
        })

        return binding.root
    }

    private fun fetchData(refreshLayout: SwipeRefreshLayout) {
        itemList = mutableListOf()
        imgList = mutableListOf()
        try {
            viewModel.getYourAnimal(userPrefs.getToken()!!, object: AnimalViewModel.ResponseCallback {
                override fun onSuccess(response: Response<ResponseBody>) {
                    Log.i("ANimal ", "Successfully...")
                    refreshLayout.isRefreshing = false
                    val data = JSONObject(response.body()!!.string())
                    val list = data.getJSONArray("data")
                    Log.i("Data : ", list.toString())
                    for(i in 0 until list.length()) {
                        val item = list.getJSONObject(i)
                        val files = item.getJSONArray("files")
                        for(j in 0 until files.length()) {
                            imgList.add(files.getJSONObject(j)["path"].toString())
                        }
                        for(k in 0 until item.getJSONObject("animalId").getJSONArray("lang").length()) {
                             if(LocaleHelper.getLocaleCode(requireActivity()) == item.getJSONObject("animalId").getJSONArray("lang").getJSONObject(k)["langCode"].toString())
                              animal = item.getJSONObject("animalId").getJSONArray("lang").getJSONObject(k)["name"].toString()
                        }
                        Log.i("animal : ", animal)
                        for(k in 0 until item.getJSONObject("breedId").getJSONArray("lang").length()) {
//                            if(LocaleHelper.getLocaleCode(requireActivity()) == item.getJSONObject("breedId").getJSONArray("lang").getJSONObject(k)["langCode"].toString())
                                breed = item.getJSONObject("breedId").getJSONArray("lang").getJSONObject(k)["name"].toString()
                        }
                        Log.i("breed : ", breed)
                        if(item.has("lactationId"))
                         for(k in 0 until item.getJSONObject("lactationId").getJSONArray("lang").length()) {
                             if(LocaleHelper.getLocaleCode(requireActivity()) == item.getJSONObject("lactationId").getJSONArray("lang").getJSONObject(k)["langCode"].toString())
                                 lactation = item.getJSONObject("lactationId").getJSONArray("lang").getJSONObject(k)["name"].toString()
                         }
//                        Log.i("lactation : ", lactation!!)
                       if(item.has("optionalData"))  {
                           if(item.getJSONObject("optionalData").has("pregnentId"))
                            for(k in 0 until item.getJSONObject("optionalData").getJSONObject("pregnentId").getJSONArray("lang").length()) {
                                if(LocaleHelper.getLocaleCode(requireActivity()) == item.getJSONObject("optionalData").getJSONObject("pregnentId").getJSONArray("lang").getJSONObject(k)["langCode"].toString())
                                    pregnent = item.getJSONObject("optionalData").getJSONObject("pregnentId").getJSONArray("lang").getJSONObject(k)["name"].toString()
                            }
                           if(item.getJSONObject("optionalData").has("animalBabyId"))
                               for(k in 0 until item.getJSONObject("optionalData").getJSONObject("animalBabyId").getJSONArray("lang").length()) {
                                if(LocaleHelper.getLocaleCode(requireActivity()) == item.getJSONObject("optionalData").getJSONObject("animalBabyId").getJSONArray("lang").getJSONObject(k)["langCode"].toString())
                                     animalBaby = item.getJSONObject("optionalData").getJSONObject("animalBabyId").getJSONArray("lang").getJSONObject(k)["name"].toString()
                               }
                           if(item.getJSONObject("optionalData").has("calfGenderId"))
                            for(k in 0 until item.getJSONObject("optionalData").getJSONObject("calfGenderId").getJSONArray("lang").length()) {
                                if(LocaleHelper.getLocaleCode(requireActivity()) == item.getJSONObject("optionalData").getJSONObject("calfGenderId").getJSONArray("lang").getJSONObject(k)["langCode"].toString())
                                   calfGender = item.getJSONObject("optionalData").getJSONObject("calfGenderId").getJSONArray("lang").getJSONObject(k)["name"].toString()
                            }
                           if(item.getJSONObject("optionalData").has("info"))
                             moreInfo = item.getJSONObject("optionalData")["info"].toString()
                       }
                       if(item.has("currentMilk"))
                           currentMilk = item["currentMilk"].toString()
                       if(item.has("milkCapacity"))
                           milkCapacity = item["milkCapacity"].toString()

                        itemList.add(
                            BuyAnimalModel(
                                item["_id"].toString(),
                                item.getJSONObject("userId")["name"].toString(),
                                item.getJSONObject("userId")["photo"].toString(),
                                item.getJSONObject("userId")["phone"].toString(),
                                animal,
                                breed,
                                lactation,
                                currentMilk,
                                milkCapacity,
                                item["price"].toString(),
                                item["isNegotiable"].toString(),
                                imgList,
                                animalBaby,
                                pregnent,
                                calfGender,
                                moreInfo,
                                item["createdAt"].toString()
                            )
                        )
                    }
                    adapter = BuyAnimalAdapter(requireContext(), itemList, false)
                    binding.recView.layoutManager = LinearLayoutManager(requireContext())
                    binding.recView.adapter = adapter
                    Log.i("Item List : ", itemList.toString())
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

    private fun fetchPrimeData(refreshLayout: SwipeRefreshLayout) {
        refreshLayout.isRefreshing = true
        itemList = mutableListOf()
        imgList = mutableListOf()
        try {
            viewModel.getYourPrimeAnimal(userPrefs.getToken()!!, object: AnimalViewModel.ResponseCallback {
                override fun onSuccess(response: Response<ResponseBody>) {
                    refreshLayout.isRefreshing = false
                    val data = JSONObject(response.body()!!.string())
                    val list = data.getJSONArray("data")
                    Log.i("List : ", list.toString())
                    for(i in 0 until list.length()) {
                        val item = list.getJSONObject(i)
                        val files = item.getJSONArray("files")
                        for(j in 0 until files.length()) {
                            imgList.add(files.getJSONObject(j)["path"].toString())
                        }
                        for(k in 0 until item.getJSONObject("animalId").getJSONArray("lang").length()) {
                            if(LocaleHelper.getLocaleCode(requireActivity()) == item.getJSONObject("animalId").getJSONArray("lang").getJSONObject(k)["langCode"].toString())
                                animal = item.getJSONObject("animalId").getJSONArray("lang").getJSONObject(k)["name"].toString()
                        }
                        Log.i("animal : ", animal)
                        for(k in 0 until item.getJSONObject("breedId").getJSONArray("lang").length()) {
//                            if(LocaleHelper.getLocaleCode(requireActivity()) == item.getJSONObject("breedId").getJSONArray("lang").getJSONObject(k)["langCode"].toString())
                            breed = item.getJSONObject("breedId").getJSONArray("lang").getJSONObject(k)["name"].toString()
                        }
                        Log.i("breed : ", breed)
                        if(item.has("lactationId"))
                            for(k in 0 until item.getJSONObject("lactationId").getJSONArray("lang").length()) {
                                if(LocaleHelper.getLocaleCode(requireActivity()) == item.getJSONObject("lactationId").getJSONArray("lang").getJSONObject(k)["langCode"].toString())
                                    lactation = item.getJSONObject("lactationId").getJSONArray("lang").getJSONObject(k)["name"].toString()
                            }
//                        Log.i("lactation : ", lactation!!)
                        if(item.has("optionalData"))  {
                            if(item.getJSONObject("optionalData").has("pregnentId"))
                                for(k in 0 until item.getJSONObject("optionalData").getJSONObject("pregnentId").getJSONArray("lang").length()) {
                                    if(LocaleHelper.getLocaleCode(requireActivity()) == item.getJSONObject("optionalData").getJSONObject("pregnentId").getJSONArray("lang").getJSONObject(k)["langCode"].toString())
                                        pregnent = item.getJSONObject("optionalData").getJSONObject("pregnentId").getJSONArray("lang").getJSONObject(k)["name"].toString()
                                }
                            if(item.getJSONObject("optionalData").has("animalBabyId"))
                                for(k in 0 until item.getJSONObject("optionalData").getJSONObject("animalBabyId").getJSONArray("lang").length()) {
                                    if(LocaleHelper.getLocaleCode(requireActivity()) == item.getJSONObject("optionalData").getJSONObject("animalBabyId").getJSONArray("lang").getJSONObject(k)["langCode"].toString())
                                        animalBaby = item.getJSONObject("optionalData").getJSONObject("animalBabyId").getJSONArray("lang").getJSONObject(k)["name"].toString()
                                }
                            if(item.getJSONObject("optionalData").has("calfGenderId"))
                                for(k in 0 until item.getJSONObject("optionalData").getJSONObject("calfGenderId").getJSONArray("lang").length()) {
                                    if(LocaleHelper.getLocaleCode(requireActivity()) == item.getJSONObject("optionalData").getJSONObject("calfGenderId").getJSONArray("lang").getJSONObject(k)["langCode"].toString())
                                        calfGender = item.getJSONObject("optionalData").getJSONObject("calfGenderId").getJSONArray("lang").getJSONObject(k)["name"].toString()
                                }
                            if(item.getJSONObject("optionalData").has("info"))
                                moreInfo = item.getJSONObject("optionalData")["info"].toString()
                        }
                        if(item.has("currentMilk"))
                            currentMilk = item["currentMilk"].toString()
                        if(item.has("milkCapacity"))
                            milkCapacity = item["milkCapacity"].toString()

                        itemList.add(
                            BuyAnimalModel(
                                item["_id"].toString(),
                                item.getJSONObject("userId")["name"].toString(),
                                item.getJSONObject("userId")["photo"].toString(),
                                item.getJSONObject("userId")["phone"].toString(),
                                animal,
                                breed,
                                lactation,
                                currentMilk,
                                milkCapacity,
                                item["price"].toString(),
                                item["isNegotiable"].toString(),
                                imgList,
                                animalBaby,
                                pregnent,
                                calfGender,
                                moreInfo,
                                item["createdAt"].toString()
                            )
                        )
                    }
                    adapter = BuyAnimalAdapter(requireContext(), itemList, false)
                    binding.recView.layoutManager = LinearLayoutManager(requireContext())
                    binding.recView.adapter = adapter
                    Log.i("Item List : ", itemList.toString())
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