package mx.itesm.thegoldenbook.ui.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import mx.itesm.thegoldenbook.R

class Ayuda : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayuda)
    }

    fun abrirPaginaAlzheimer(v:View){
        val url = "https://alzheimermexico.org.mx/"
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}