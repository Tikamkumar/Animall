package com.online.animall.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.online.animall.LocationHelper
import com.online.animall.R
import com.online.animall.data.local.UserPreferences
import com.online.animall.data.model.CreateUserResponse
import com.online.animall.data.remote.RetrofitClient
import com.online.animall.databinding.ActivityTakeLocationBinding
import com.online.animall.home.MainActivity
import com.online.animall.presentation.viewmodel.UserViewModel
import com.online.animall.signup.VerifyMobile
import com.online.animall.utils.SnackbarUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TakeLocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTakeLocationBinding
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private val PERMISSION_ID: Int = 44
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTakeLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        userPreferences = UserPreferences(this)

        binding.root.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.getLocation.setOnClickListener {
            getLastLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                mFusedLocationClient?.lastLocation
                    ?.addOnCompleteListener { task ->
                        val location: Location? = task.result
                        Toast.makeText(this, "Location : ${location?.latitude}", Toast.LENGTH_SHORT).show()
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            Toast.makeText(this, "Latitude : ${location?.latitude}", Toast.LENGTH_SHORT).show()
                            Log.i("Longitude : ", "${location.longitude}")
                            Log.i("Latitude : ", "${location.latitude}")
                         /*   try {
                                *//*userViewModel.locationResponse.observe(this) { response ->
                                    if(response != null) {
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        SnackbarUtil.error(binding.main)
                                    }
                                }*//*
                                userViewModel.sendLocation(location.longitude.toString(), location.latitude.toString(), userPreferences.getToken()!!, object: UserViewModel.LocationCallback {
                                    override fun onSuccess(response: CreateUserResponse) {
                                        val intent = Intent(this@TakeLocationActivity, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }

                                    override fun onError(error: String) {
                                        SnackbarUtil.error(binding.main)
                                    }
                                })
                            } catch(e: Exception) {
                                SnackbarUtil.error(binding.main)
                            }*/
/*
                           CoroutineScope(Dispatchers.IO).launch {
                               val response = RetrofitClient.api.saveLocation("Bearer " + userPreferences.getToken()!!, com.online.animall.data.model.LocationRequest("${location.latitude}", "${location.latitude}"))

                                if (response.isSuccessful) {
                                    // Handle the successful response
                                    val userResponse = response.body()
                                    if (userResponse != null) {
                                        // Process userResponse
                                        Log.d("API Success", "Response: $userResponse")
                                    }
                                } else {
                                    // Handle the error response
                                    val errorBody = response.errorBody()?.string()
                                    Log.e("API Error", "Code: ${response.code()}, Body: $errorBody")
                                }
                            }*/

                        }
                    }
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG)
                    .show()
                val intent: Intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = com.google.android.gms.location.LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(5)
        mLocationRequest.setFastestInterval(0)
        mLocationRequest.setNumUpdates(1)


        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient?.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            Log.i("Latitude: ", "${mLastLocation.latitude}")
            Log.i("Longit: ", "${mLastLocation.longitude}")
           /* latitudeTextView.setText(("Latitude: " + mLastLocation.getLatitude()).toString() + "")
            longitTextView.setText(("Longitude: " + mLastLocation.getLongitude()).toString() + "")*/
        }
    }

    // method to check for permissions
    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
   }

    // method to request for permissions
    private fun requestPermission() {
        if(userPreferences.isfirstTimeLocation()) {
            userPreferences.setFirstTimeLocation()
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID
            )
        } else {
            showPermissionDeniedDialog()
        }
        /*if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show an explanation to the user
            Toast.makeText(this, "Inside shouldShow", Toast.LENGTH_LONG).show()

            showPermissionRationale()
        } else {
            Toast.makeText(this, "Inside", Toast.LENGTH_LONG).show()
            if(userPreferences.isfirstTimeLocation()) {
                userPreferences.setFirstTimeLocation()
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID
                )
            } else {
                showPermissionDeniedDialog()
            }
        }*/
    }

    private fun showPermissionRationale() {
        AlertDialog.Builder(this)
            .setTitle("Location Permission Needed")
            .setMessage("This app requires location permission to function properly.")
            .setPositiveButton("OK") { _, _ ->
                // Request the permission again
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Allow Permission")
            .setMessage("Location permission is required for this app. You can enable it in the app settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                // Open app settings
                openAppSettings()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    // If everything is alright then
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }


}