package com.online.animall.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.online.animall.R
import com.online.animall.adapter.ImgAdapter
import com.online.animall.adapter.SingleChoiceAdapter
import com.online.animall.data.local.UserPreferences
import com.online.animall.data.model.SellAnimalResponse
import com.online.animall.databinding.FragmentSellAnimalFormBinding
import com.online.animall.presentation.dialog.LoadingDialog
import com.online.animall.presentation.viewmodel.AnimalViewModel
import com.online.animall.utils.SnackbarUtil
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class SellAnimalFormFragment : Fragment() {

    private lateinit var binding: FragmentSellAnimalFormBinding
    private var fetchImgs: MutableList<Uri> = mutableListOf()
    private lateinit var imgAdapter: ImgAdapter
    private val animalViewModel: AnimalViewModel by viewModels()
    private lateinit var userPref: UserPreferences
    private lateinit var animalName: MutableList<String>
    private lateinit var animalData: JSONArray
    private lateinit var breedData: JSONArray
    private var selectedAnimal: String? = null
    private lateinit var breedList: MutableList<String>
    private lateinit var animalGender: String
    private var currentMilk: String = ""
    private var milkCapacity: String = ""
    private var price: String = ""
    private var info: String = ""
    private lateinit var animalId: String
    private lateinit var breedId: String
    private var selectLactation: String = ""
    private var animalBaby: String = ""
    private var pregnent: String = ""
    private var calfGender: String = ""
    private var isNegotiable: Boolean = false
    private val viewModel: AnimalViewModel by viewModels()
    private lateinit var loader: LoadingDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animalViewModel.getAnimals(userPref.getToken()!!, object: AnimalViewModel.GetAnimalCallback {
            override fun onSuccess(response: Response<ResponseBody>) {
                val data = JSONObject(response.body()!!.string())
                animalData = data.getJSONArray("data")
                for (animal in 0 until animalData.length()) {
                    animalName.add(animalData.getJSONObject(animal)["name"].toString())
                }
                val adapter: ArrayAdapter<String> =
                    ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        animalName
                    )
                binding.selectAnimal.adapter = adapter
            }

            override fun onError(error: String) {
                Log.e("Error: ", error.toString())
            }

        })
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSellAnimalFormBinding.bind(
            inflater.inflate(
                R.layout.fragment_sell_animal_form,
                container,
                false
            )
        )
        userPref = UserPreferences(requireContext())
        setUpImgRecView()

        animalName = mutableListOf()
        animalData = JSONArray()
        breedList = mutableListOf(getString(R.string.select_breed))
        breedData = JSONArray()
        loader = LoadingDialog(requireContext())

        binding.showHideMoreInfo.setOnClickListener {
            showHideInfoLayout()
        }

        binding.selectPhoto.setOnClickListener {
            fetchPhoto()
        }
        binding.subBtn.setOnClickListener {
            checkValidation()
        }

        spinner(binding.selectBreed, breedList)

        showSpinner(binding.selectLactation, resources.getStringArray(R.array.lactation_option))
        showSpinner(binding.isPregnent, resources.getStringArray(R.array.yes_no_option))
        showSpinner(binding.pregnentTime, resources.getStringArray(R.array.pregnant_time))
        showSpinner(binding.isCalfAvailable, resources.getStringArray(R.array.calf_option))
        showSpinner(binding.isAnimalDeliever, resources.getStringArray(R.array.yes_no_option))
        showSpinner(binding.delieveryTime, resources.getStringArray(R.array.time_duration))


        binding.isAnimalDeliever.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        binding.delieveryTime.visibility = View.GONE
                        animalBaby = ""
                    }
                    1 -> {
                        animalBaby = binding.delieveryTime.selectedItem.toString()
                        binding.delieveryTime.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.delieveryTime.visibility = View.GONE
                        animalBaby = "No"
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.selectBreed.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                try {
                    breedId = breedData.getJSONObject(position)["_id"].toString()
                    try {
                        animalGender = breedData.getJSONObject(position)["gender"].toString()
                        Toast.makeText(requireContext(), animalGender, Toast.LENGTH_SHORT).show()
                    } finally {
                        Toast.makeText(requireContext(), animalGender, Toast.LENGTH_SHORT).show()
                        showOrHideGenderField(animalGender)
                        Log.i("Breed Id: ", breedId)
                    }
                } catch(exp: Exception) {
                    Log.e("Error: ", exp.toString())
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.isCalfAvailable.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position) {
                    0 -> calfGender = ""
                    else -> calfGender = binding.isCalfAvailable.selectedItem.toString()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.isPregnent.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        pregnent = ""
                        binding.pregnentTime.visibility = View.GONE
                    }
                    1 -> {
                        pregnent = binding.pregnentTime.selectedItem.toString()
                        binding.pregnentTime.visibility = View.VISIBLE
                    }
                    else -> {
                        pregnent = "No"
                        binding.pregnentTime.visibility = View.GONE
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.selectAnimal.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                try {
                    animalId = animalData.getJSONObject(position)["_id"].toString()
                    animalGender = animalData.getJSONObject(position)["gender"].toString()
                    Log.i("Gender :", animalGender)
                    Toast.makeText(requireContext(), animalGender, Toast.LENGTH_SHORT).show()
                    showOrHideGenderField(animalGender)
                    Log.i("Data: ", animalId.toString())
                    breedList.clear()
                    breedData = JSONArray()
                    breedId = ""
                    getAnimalBreed(animalId)
                } catch (e: Exception) {
                    Log.e("Error: ", e.message.toString())
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        return binding.root
    }

    private fun checkValidation() {
        selectLactation = binding.selectLactation.selectedItem.toString()
        currentMilk = binding.currentMilk.text.toString()
        milkCapacity = binding.milkCapacity.text.toString()
        price = binding.price.text.toString()
        info = binding.moreInfo.text.toString()
        isNegotiable = binding.selectNegotiable.isChecked

        when(animalGender.lowercase()) {
            "female" -> {
                if(currentMilk.isEmpty() && milkCapacity.isEmpty() && price.isEmpty()) {
                    SnackbarUtil.error(binding.root, "Fill all mandatory(*) details..")
                    return
                }
            }
            "male" -> {
                selectLactation = ""
                currentMilk = ""
                milkCapacity = ""
                info = ""
                pregnent = ""
                calfGender = ""
                animalBaby = ""
                if(price.isEmpty()) {
                    SnackbarUtil.error(binding.root, "Fill the Price..")
                    return
                }
            }
        }
        if(fetchImgs.isEmpty()) {
            SnackbarUtil.error(binding.root, "Minimum 1 photo Required..")
            return
        }
        uploadAnimal()

    }

    private fun showSpinner(spinner: Spinner, list: Array<String>) {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            list
        )
        spinner.adapter = adapter
    }

    private fun setUpImgRecView() {
        imgAdapter = ImgAdapter(fetchImgs)
        binding.recView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recView.adapter = imgAdapter
    }

    private fun showHideInfoLayout() {
        if (binding.fillMoreInfoLayout.visibility == View.VISIBLE) {
            binding.fillMoreInfoLayout.visibility = View.GONE
            binding.root.post(Runnable { binding.root.smoothScrollTo(0, binding.root.bottom) })
        } else {
            binding.fillMoreInfoLayout.visibility = View.VISIBLE
            binding.root.post(Runnable { binding.root.smoothScrollTo(0, binding.root.bottom) })
        }
    }


    @SuppressLint("MissingInflatedId")
    private fun showDialog() {
        val dialog = Dialog(requireContext())
        val view = layoutInflater.inflate(R.layout.selection_dialog, null)
        val recView = view.findViewById<RecyclerView>(R.id.rec_view)
        val adapter = SingleChoiceAdapter(requireContext(), animalName, dialog) { selected ->
            selectedAnimal = animalName[selected]
//            binding.selectAnimal.setText(selectedAnimal)
            val animalId: String = animalData.getJSONObject(selected)["_id"].toString()
            animalGender = animalData.getJSONObject(selected)["gender"].toString()
            Log.i("Gender :", animalGender)
            showOrHideGenderField(animalGender)
            Log.i("Data: ", animalId.toString())
            breedList.clear()
            getAnimalBreed(animalId)
        }
        recView.layoutManager = LinearLayoutManager(requireContext())
        recView.adapter = adapter
        dialog.setContentView(view)
        dialog.show()
    }

    fun spinner(spinner: Spinner, list: MutableList<String>) {
        if (list.isEmpty()) list.add(getString(R.string.select))
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                breedList
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun getAnimalBreed(animalId: String) {
        animalViewModel.getAnimalBreed(
            userPref.getToken()!!,
            null,
            animalId,
            object : AnimalViewModel.GetAnimalBreedCallback {
                override fun onSuccess(response: Response<ResponseBody>) {
                    if (response != null) {
                        val data = JSONObject(response.body()!!.string())
                        val animalData = data.getJSONArray("data")
                        breedData = animalData
                        Log.i("Data : ", animalData.toString())
                        for (animal in 0 until animalData.length()) {
                            breedList.add(animalData.getJSONObject(animal)["name"].toString())
                        }
                        spinner(binding.selectBreed, breedList)
                    }
                }
                override fun onError(error: String) {
                    Log.e("Error : ", error)
                }
            })
    }

    private fun fetchPhoto() {
        if(fetchImgs.size == 4) {
            SnackbarUtil.error(binding.root, "Maximum 4 Photo Allowed..")
            return
        }
        ImagePicker.with(this)
            .cropSquare()
            .compress(1024)
            .createIntent { intent -> startForProfileImageResult.launch(intent) }
    }


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val uri = data?.data!!
                    try {
                        val validMimeTypes = listOf("jpeg", "png", "jpg")
                        if(uri.toString().substringAfterLast(".") in validMimeTypes) {
                            fetchImgs.add(uri)
                            imgAdapter.updateList(fetchImgs)
                            Log.e("Size of List : ", fetchImgs.size.toString())
                        } else {
                            SnackbarUtil.error(binding.root, "Invalid Image format..")
                        }
                    } catch (exp: Exception) {
                        Log.e("Error: ", exp.message.toString())
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun showOrHideGenderField(gender: String) {
        if (gender.lowercase() == "male") {
            binding.showHideMoreInfo.visibility = View.GONE
            binding.fillMoreInfoLayout.visibility = View.GONE
            binding.fieldFemale.visibility = View.GONE
        } else {
            binding.showHideMoreInfo.visibility = View.VISIBLE
            binding.fieldFemale.visibility = View.VISIBLE
        }
    }

    private fun uploadAnimal() {
        loader.show()

        try {
            val request = SellAnimalResponse(
                animalId.toRequestBody("text/plain".toMediaTypeOrNull()),
                breedId.toRequestBody("text/plain".toMediaTypeOrNull()),
                selectLactation.toRequestBody("text/plain".toMediaTypeOrNull()),
                currentMilk.toRequestBody("text/plain".toMediaTypeOrNull()),
                milkCapacity.toRequestBody("text/plain".toMediaTypeOrNull()),
                price.toRequestBody("text/plain".toMediaTypeOrNull()),
                "video".toRequestBody("text/plain".toMediaTypeOrNull()),
                "image".toRequestBody("text/plain".toMediaTypeOrNull()),
                "$isNegotiable".toRequestBody("text/plain".toMediaTypeOrNull()),
                "false".toRequestBody("text/plain".toMediaTypeOrNull()),
                animalBaby.toRequestBody("text/plain".toMediaTypeOrNull()),
                info.toRequestBody("text/plain".toMediaTypeOrNull()),
                pregnent.toRequestBody("text/plain".toMediaTypeOrNull()),
                calfGender.toRequestBody("text/plain".toMediaTypeOrNull()),
            )

            viewModel.uploadSellAnimal(
                userPref.getToken()!!,
                getFiles(),
                request,
                object : AnimalViewModel.SellAnimalCallback {
                    override fun onSuccess(response: Response<ResponseBody>) {
                        Log.i("Response: ", response.body()!!.string())
                        SnackbarUtil.success(binding.root, " Animal Successfully Uploaded..")
                        resetForm()
                        loader.hide()
                    }
                    override fun onError(error: String) {
                        Log.e("Error: ", error)
                        SnackbarUtil.error(binding.root)
                        loader.hide()
                    }
                })
        } catch (exp: Exception) {
            SnackbarUtil.error(binding.root)
        }

    }

    private fun getFiles(): List<MultipartBody.Part> {
        val list: MutableList<MultipartBody.Part> = mutableListOf()
        for(item in fetchImgs) {
            val fileDir = requireContext().applicationContext.filesDir
            val file = File(fileDir, item.toString().substringAfterLast("/"))
            val inputStream = requireContext().contentResolver.openInputStream(item)
            val outputStream = FileOutputStream(file)
            inputStream!!.copyTo(outputStream)

            val requestBody = file.asRequestBody("image/jpg".toMediaTypeOrNull())
            val multipart = MultipartBody.Part.createFormData("file", file.name, requestBody)
            list.add(multipart)
        }
        return list.toList()
    }

    private fun resetForm() {
        binding.moreInfo.text.clear()
        binding.currentMilk.text.clear()
        binding.milkCapacity.text.clear()
        binding.price.text.clear()
        binding.selectNegotiable.isChecked = false
        fetchImgs.clear()
        imgAdapter.updateList(fetchImgs)
    }

}