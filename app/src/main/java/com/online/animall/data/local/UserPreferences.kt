package com.online.animall.data.local

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "token"
        private const val USER_ID = "userId"
        private const val FIRST_TIME_LOCATION = "location"
    }

    fun saveData(token: String, userId: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_TOKEN, token)
        editor.putString(USER_ID, userId)
        editor.apply()
    }

    fun setFirstTimeLocation() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(FIRST_TIME_LOCATION, false)
        editor.apply()
    }

    fun isfirstTimeLocation(): Boolean = sharedPreferences.getBoolean(FIRST_TIME_LOCATION, true)

    fun getToken(): String?  = sharedPreferences.getString(KEY_TOKEN, null)

    fun getUserId(): String? = sharedPreferences.getString(USER_ID, null)

}