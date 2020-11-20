package mx.itesm.thegoldenbook.utils

import android.util.Log
import androidx.annotation.NonNull
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        fun print(@NonNull text: String) {
            if(text.isEmpty()) {
                Log.d("Jaime", "Text empty")
            } else {
                Log.d("Jaime", text)
            }
        }

        fun getFormattedDate(timestamp: Long): String {
            val date = Date(timestamp)
            return try {
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
                simpleDateFormat.isLenient = false
                simpleDateFormat.format(date)
            } catch (ex: Exception) {
                "Invalid date"
            }
        }
    }
}