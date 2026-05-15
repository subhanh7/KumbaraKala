package com.kumbarakala.app.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefHelper(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(
            "kumbarakala_prefs",
            Context.MODE_PRIVATE
        )

    fun saveString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getString(key: String): String {
        return prefs.getString(key, "") ?: ""
    }
}