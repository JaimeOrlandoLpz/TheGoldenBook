package mx.itesm.thegoldenbook.utils

import android.util.Log
import androidx.annotation.NonNull

class Utils {
    companion object {
        fun print(@NonNull text: String) {
            if(text.isEmpty()) {
                Log.d("Jaime", "Text empty")
            } else {
                Log.d("Jaime", text)
            }
        }
    }
}