package com.online.animall

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.LOCATION_SERVICE
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.online.animall.data.local.UserPreferences
import com.online.animall.data.model.CreateUserResponse
import com.online.animall.home.MainActivity
import com.online.animall.presentation.viewmodel.UserViewModel
import com.online.animall.utils.SnackbarUtil

class LocationHelper(private val context: Context, private val userViewModel: UserViewModel): AppCompatActivity() {

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private val PERMISSION_ID: Int = 44
    private val userPreferences = UserPreferences(context)

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        Toast.makeText(this, "Call First Method()", Toast.LENGTH_SHORT).show()
        if (checkPermission()) {
            if (isLocationEnabled()) {
                mFusedLocationClient?.lastLocation
                    ?.addOnCompleteListener { task ->
                        val location: Location? = task.result
                        Toast.makeText(this, "Longtitude : ${location?.longitude}", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, "Latitude : ${location?.latitude}", Toast.LENGTH_SHORT).show()
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            Log.i("Longitude : ", "${location.longitude}")
                            Log.i("Latitude : ", "${location.latitude}")
                            /*try {
                                userViewModel.sendLocation(location.longitude.toString(), location.latitude.toString(), userPreferences.getToken()!!, object: UserViewModel.LocationCallback {
                                    override fun onSuccess(response: CreateUserResponse) {
                                        val intent = Intent(context, MainActivity::class.java)
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(context, intent, null)
                                    }
                                    override fun onError(error: String) {
                                        Toast.makeText(context, "Location not update..", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            } catch(e: Exception) {
                                Toast.makeText(context, "Location not update..", Toast.LENGTH_SHORT).show()
                            }*/
                        }
                    }
            } else {
                Toast.makeText(context, "Please turn on" + " your location...", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        Log.i("Call", "Call New Location Data..")
        val mLocationRequest = com.google.android.gms.location.LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(5)
        mLocationRequest.setFastestInterval(0)
        mLocationRequest.setNumUpdates(1)


        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
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
        }
    }

    // method to check for permissions
    private fun checkPermission(): Boolean {
        Log.i("Call", "Call Check Permission")
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private fun requestPermission() {
        Log.i("Call", "Call Request Permission")
        if(userPreferences.isfirstTimeLocation()) {
            userPreferences.setFirstTimeLocation()
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID
            )
        } else {
            showPermissionDeniedDialog()
        }
    }

    /*private fun showPermissionRationale() {
        AlertDialog.Builder(context)
            .setTitle("Location Permission Needed")
            .setMessage("context app requires location permission to function properly.")
            .setPositiveButton("OK") { _, _ ->
                // Request the permission again
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }*/

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(context)
            .setTitle("Allow Permission")
            .setMessage("Location permission is required for context app. You can enable it in the app settings.")
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
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
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