package com.online.animall

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.app.ActivityCompat.recreate
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
