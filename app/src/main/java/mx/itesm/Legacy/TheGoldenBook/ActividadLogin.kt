package mx.itesm.Legacy.TheGoldenBook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ActividadLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginClicked(view: View){
        val intentMenu = Intent(this, ActividadMenu2::class.java).apply {

        }

        startActivity(intentMenu)
    }
}