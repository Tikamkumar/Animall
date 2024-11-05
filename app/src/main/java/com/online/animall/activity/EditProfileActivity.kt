package com.online.animall.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import com.online.animall.LocaleHelper
import com.online.animall.R
import com.online.animall.data.local.UserPreferences
import com.online.animall.data.model.UpdateProfile
import com.online.animall.databinding.ActivityEditProfileBinding
import com.online.animall.presentation.viewmodel.UserViewModel
import com.online.animall.signup.EnterMobileActivity
import com.online.animall.utils.SnackbarUtil
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var userPrefs: UserPreferences
    private lateinit var workList: MutableList<Map<String, String>>
    private lateinit var educationLevelList: MutableList<Map<String, String>>
    private lateinit var animalHusbandryList: MutableList<Map<String, String>>
    private lateinit var reasonsList: MutableList<Map<String, String>>
    private var fetchImg = ""
    private var workId: String = ""
    private var animalHusbandryId: String = ""
    private var reasonId: String = ""
    private var educationLevelId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userPrefs = UserPreferences(this)

//        fetchData()

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.choose_lang))

        binding.selectLanguage.adapter = adapter

        binding.updateProfile.setOnClickListener {
            updateProfile()
        }

        when(LocaleHelper.getLocaleCode(this)) {
            "hi" -> binding.selectLanguage.setSelection(0)
            "en" -> binding.selectLanguage.setSelection(1)
        }

        /*binding.getStartedBtn.setOnClickListener {
            when(binding.selectLanguage.selectedItem) {
                "Hindi" -> LocaleHelper.setLocale("hi", this)
                "English" -> LocaleHelper.setLocale("hi", this)
            }
            startActivity(Intent(this, EnterMobileActivity::class.java))
        }*/

        binding.icBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.icCamera.setOnClickListener {
            fetchPhoto()
        }
        binding.pickDob.setOnClickListener {
            datePicker()
        }

        /*getUserProfession()
        getEducationLevel()
        getAnimalHusbandry()
        getReasonUsingApp()*/
    }

    private fun updateProfile() {
        try {
            val updateProfileModel = UpdateProfile(
                binding.name.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                binding.address.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                binding.whatsNo.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                binding.pickDob.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                binding.noOfAnimal.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                workId.toRequestBody("text/plain".toMediaTypeOrNull()),
                animalHusbandryId.toRequestBody("text/plain".toMediaTypeOrNull()),
                reasonId.toRequestBody("text/plain".toMediaTypeOrNull()),
                educationLevelId.toRequestBody("text/plain".toMediaTypeOrNull()),
                getFile()
            )
            viewModel.updateProfile(userPrefs.getToken()!!, updateProfileModel, object: UserViewModel.ResponseCallback {
                override fun onSuccess(response: Response<ResponseBody>) {
                    SnackbarUtil.success(binding.root, "Profile Updated Successfully..")
                    when(binding.selectLanguage.selectedItem) {
                        "Hindi" -> LocaleHelper.setLocale("hi", this@EditProfileActivity)
                        "English" -> LocaleHelper.setLocale("en", this@EditProfileActivity)
                    }
                    fetchData()
                }
                override fun onError(error: String) {
                    Log.e("Error : ", error)
                }
            }
            )
        } catch (exp: Exception) {
           Log.i("Error : ", exp.message.toString())
        }
    }

    private fun getFile(): MultipartBody.Part {
            if(fetchImg == "") return MultipartBody.Part.createFormData("images", null, RequestBody.create(
                "image/jpg".toMediaTypeOrNull(), ""))
            val fileDir = applicationContext.filesDir
            val file = File(fileDir, fetchImg.substringAfterLast("/"))
            val inputStream = contentResolver.openInputStream(Uri.parse(fetchImg))
            val outputStream = FileOutputStream(file)
            inputStream!!.copyTo(outputStream)
            val requestBody = file.asRequestBody("image/jpg".toMediaTypeOrNull())
            val multipart = MultipartBody.Part.createFormData("images", file.name, requestBody)
            return multipart
    }


    private fun fetchPhoto() {
        ImagePicker.with(this)
            .cropSquare()
            .compress(1024)
            .createIntent { intent -> startForProfileImageResult.launch(intent) }
    }

    private fun fetchData() {
        viewModel.getUserProfile(userPrefs.getToken()!!, object:  UserViewModel.ResponseCallback {
            override fun onSuccess(response: Response<ResponseBody>) {
                val data = JSONObject(response.body()!!.string()).getJSONObject("data")
                updateUI(data)
            }
            override fun onError(error: String) {
               Log.e("Error : ", error)
            }
        })
    }

    private fun updateUI(data: JSONObject) {
        Log.i("Response : ", data.toString())
       binding.name.setText("${data["name"].toString()}")
        binding.phoneNo.setText(data["phone"].toString())
        if(data["whatsUpNumber"].toString() != "null") binding.whatsNo.setText(data["whatsUpNumber"].toString())
        if(data["photo"].toString() != "") {
            Picasso.get()
                .load("http://192.168.1.9:5001/uploads/${data["photo"].toString()}")
                /*.placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)*/
                .into(binding.profileImage);
        }
        if(data["birthday"].toString() != "null") binding.pickDob.setText(data["birthday"].toString())
        binding.noOfAnimal.setText(data["numberOfAnimal"].toString())
        val address = LocaleHelper.getAddress(this, data.getJSONObject("location").getJSONArray("coordinates").getDouble(1), data.getJSONObject("location").getJSONArray("coordinates").getDouble(0))
        binding.address.setText(address)
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
                            binding.profileImage.setImageURI(uri)
                            fetchImg = uri.toString()
                        } else {
                            SnackbarUtil.error(binding.root, "Invalid Image format..")
                        }
                    } catch (exp: Exception) {
                        Log.e("Error: ", exp.message.toString())
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun datePicker() {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    binding.pickDob.setText("$dayOfMonth/${monthOfYear+1}/$year")
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

    private fun getUserProfession() {
        workList = mutableListOf()
        try {
            viewModel.getUserWorklist(userPrefs.getToken()!!,object: UserViewModel.ResponseCallback {
                override fun onSuccess(response: Response<ResponseBody>) {
                   val array = JSONObject(response.body()!!.string()).getJSONArray("data")
                    Log.i("Data : ", array.toString())
                    for(range in 0 until array.length()) {
                        val langArray = array.getJSONObject(range).getJSONArray("lang")

                        for(i in 0 until langArray.length()) {
                           if(langArray.getJSONObject(i).getString("langCode").toString() == LocaleHelper.getLocaleCode(this@EditProfileActivity)) {
                                workList.add( mapOf(
                                    array.getJSONObject(range).getString("_id").toString() to
                                    langArray.getJSONObject(i).getString("name").toString()
                                  )
                                )
                            }
                        }
                    }

                    showSpinner(workList.map { map -> map.entries.joinToString(", ") { "${it.value}" } }.toMutableList() , binding.selectProfession)

                    binding.selectProfession.onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            workId = workList[position].keys.joinToString(", ")
                            Log.i("Work Id : ", workId)
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                }
                override fun onError(error: String) {
                    Log.e("Error : ", error)
                }
            })
        } catch (exp: Exception) {
            Log.e("Error : ", exp.message.toString())
        }
    }
    private fun getAnimalHusbandry() {
        animalHusbandryList = mutableListOf()
        try {
            viewModel.getAnimalHusbandry(userPrefs.getToken()!!,object: UserViewModel.ResponseCallback {
                override fun onSuccess(response: Response<ResponseBody>) {
                   val array = JSONObject(response.body()!!.string()).getJSONArray("data")
                    for(range in 0 until array.length()) {
                        val langArray = array.getJSONObject(range).getJSONArray("lang")
                        for(i in 0 until langArray.length()) {
                            if(langArray.getJSONObject(i).getString("langCode").toString() == LocaleHelper.getLocaleCode(this@EditProfileActivity)) {
                                animalHusbandryList.add( mapOf(
                                    array.getJSONObject(range).getString("_id").toString() to
                                            langArray.getJSONObject(i).getString("name").toString()
                                 )
                                )
                            }
                        }
                    }
                    showSpinner( animalHusbandryList.map { map -> map.entries.joinToString(", ") { "${it.value}" } }.toMutableList(), binding.animalHusbandry)
                    binding.animalHusbandry.onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            animalHusbandryId = animalHusbandryList[position].keys.joinToString(", ")
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                }
                override fun onError(error: String) {
                    Log.e("Error : ", error)
                }
            })
        } catch (exp: Exception) {
            Log.e("Error : ", exp.message.toString())
        }
    }
    private fun getEducationLevel() {
        educationLevelList = mutableListOf()
        try {
            viewModel.getEducationLevels(userPrefs.getToken()!!,object: UserViewModel.ResponseCallback {
                override fun onSuccess(response: Response<ResponseBody>) {
                   val array = JSONObject(response.body()!!.string()).getJSONArray("data")
                    for(range in 0 until array.length()) {
                        val langArray = array.getJSONObject(range).getJSONArray("lang")
                        for(i in 0 until langArray.length()) {
                            if(langArray.getJSONObject(i).getString("langCode").toString() == LocaleHelper.getLocaleCode(this@EditProfileActivity)) {
                                educationLevelList.add( mapOf(
                                    array.getJSONObject(range).getString("_id").toString() to
                                            langArray.getJSONObject(i).getString("name").toString()
                                )
                                )
                            }
                        }
                    }
                    showSpinner(educationLevelList.map { map -> map.entries.joinToString(", ") { "${it.value}"} }.toMutableList(), binding.selectEducation)
                    binding.selectEducation.onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            educationLevelId = educationLevelList[position].keys.joinToString(", ")
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                }
                override fun onError(error: String) {
                    Log.e("Error : ", error)
                }
            })
        } catch (exp: Exception) {
            Log.e("Error : ", exp.message.toString())
        }
    }
    private fun getReasonUsingApp() {
        reasonsList = mutableListOf()
        try {
            viewModel.getReasonOfUsingApp(userPrefs.getToken()!!,object: UserViewModel.ResponseCallback {
                override fun onSuccess(response: Response<ResponseBody>) {
                   val array = JSONObject(response.body()!!.string()).getJSONArray("data")
                    for(range in 0 until array.length()) {
                        val langArray = array.getJSONObject(range).getJSONArray("lang")
                        for(i in 0 until langArray.length()) {
                            if(langArray.getJSONObject(i).getString("langCode").toString() == LocaleHelper.getLocaleCode(this@EditProfileActivity)) {
                                reasonsList.add( mapOf(
                                    array.getJSONObject(range).getString("_id").toString() to
                                            langArray.getJSONObject(i).getString("name").toString()
                                )
                                )
                            }
                        }
                    }
                    showSpinner(reasonsList.map { map -> map.entries.joinToString(", ") { "${it.value}"} }.toMutableList(), binding.selectReason)
                    binding.selectReason.onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            reasonId = reasonsList[position].keys.joinToString(", ")
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                }
                override fun onError(error: String) {
                    Log.e("Error : ", error)
                }
            })
        } catch (exp: Exception) {
            Log.e("Error : ", exp.message.toString())
        }
    }

    private fun showSpinner(list: MutableList<String>, spinner: Spinner) {
        if(list.isEmpty()) list.add(resources.getString(R.string.select))
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list)
        spinner.adapter = adapter
    }
}