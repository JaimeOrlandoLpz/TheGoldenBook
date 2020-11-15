package mx.itesm.thegoldenbook.application

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.managers.PreferenceManager
import mx.itesm.thegoldenbook.models.Owner

class Settings {
    companion object {
        fun setLogged(logged: Boolean) {
            PreferenceManager.instance.set(R.string.key_user_logged, logged)
        }

        fun isLogged(): Boolean {
            return PreferenceManager.instance.getBoolean(R.string.key_user_logged)
        }

        fun setCurrentUser(owner: Owner) {
            val gson = Gson()
            val json = gson.toJson(owner)
            PreferenceManager.instance.set(R.string.key_current_user, json)
        }

        fun getCurrentUser(): Owner {
            val gson = Gson()
            val type = object : TypeToken<Owner>() {}.type
            val json = PreferenceManager.instance.getString(R.string.key_current_user)
            return gson.fromJson(json, type)
        }
    }
}