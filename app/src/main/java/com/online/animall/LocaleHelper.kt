package com.online.animall

import android.app.Activity
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import java.util.Locale


object LocaleHelper {
    fun setLocale(languageCode: String, activity: Activity) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = activity.resources.configuration
        config.setLocale(locale)

        // Update the configuration of the app context
        activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
        val sharedPreferences = activity.getSharedPreferences("app_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("language", languageCode)
        editor.apply()
        // Optionally restart the activity to apply the changes
//        recreate(activity)
    }
    fun getLocale(activity: Activity) {
        val sharedPreferences = activity.getSharedPreferences("app_prefs", MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("language", "hi")

        if (Locale.getDefault().language != savedLanguage) {
            setLocale(savedLanguage ?: "hi", activity)  // Recreate the activity to apply the new locale
        }
    }

    fun getLocaleCode(activity: Activity): String {
        val sharedPreferences = activity.getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("language", "hi")!!
    }

    fun getAddress(context: Context, latitude: Double, longitude: Double): String {
        val addresses: List<Address>
        val geocoder = Geocoder(context, Locale.getDefault())
        addresses = geocoder.getFromLocation(
            latitude,
            longitude,
            1
        ) as List<Address>
        val address: String =
            addresses!![0].getAddressLine(0)
       /* val city: String = addresses!![0].locality
        val state: String = addresses!![0].adminArea
        val country: String = addresses!![0].countryName
        val postalCode: String = addresses!![0].postalCode
        val knownName: String = addresses!![0].featureName*/
        Log.i("address", address)
        /*Log.i("city", city)
        Log.i("State", state)
        Log.i("Country", country)
        Log.i("pinCode", postalCode)
        Log.i("KnownName", knownName)*/
        return address
    }
    /*private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

    // Method to set the language at runtime
    fun setLocale(context: Context, language: String): Context {
        persist(context, language)

        // Updating the language for devices above Android Nougat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language)
        }
        // For devices with lower versions of Android OS
        return updateResourcesLegacy(context, language)
    }

    private fun persist(context: Context, language: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(SELECTED_LANGUAGE, language)
        editor.apply()
    }

    // Method to update the language of the application by creating
    // an object of the inbuilt Locale class and passing the language argument to it
    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

    @Suppress("deprecation")
    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale

        configuration.setLayoutDirection(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)

        return context
    }

    fun getLocale(context: Context): Locale {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val language = preferences.getString(SELECTED_LANGUAGE, Locale.getDefault().language)
        return Locale(language ?: Locale.getDefault().language)
    }*/
}
