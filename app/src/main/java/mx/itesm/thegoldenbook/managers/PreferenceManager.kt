package mx.itesm.thegoldenbook.managers

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.annotation.NonNull

class PreferenceManager private constructor() {
    companion object {
        var instance: PreferenceManager = PreferenceManager()

        val preferencesName = "TheGoldenBook"

        lateinit var preferences: SharedPreferences
        lateinit var resources: Resources
    }

    fun init(@NonNull context: Context) {
        preferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        resources = context.resources
    }

    fun removeAll() {
        preferences.edit().clear().apply()
    }

    fun set(key: Int, value: String) {
        preferences.edit().putString(resources.getString(key), value).apply()
    }

    fun set(key: Int, value: Int) {
        preferences.edit().putInt(resources.getString(key), value).apply()
    }

    fun set(key: Int, value: Boolean) {
        preferences.edit().putBoolean(resources.getString(key), value).apply()
    }

    fun getString(key: Int): String {
        return preferences.getString(resources.getString(key), "")!!
    }

    fun getBoolean(key: Int): Boolean {
        return preferences.getBoolean(resources.getString(key), false)
    }

    fun getInt(key: Int): Int {
        return preferences.getInt(resources.getString(key), 0)
    }
}