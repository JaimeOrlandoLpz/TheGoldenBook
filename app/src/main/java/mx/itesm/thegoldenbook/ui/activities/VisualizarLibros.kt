package mx.itesm.thegoldenbook.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import mx.itesm.thegoldenbook.R

class VisualizarLibros : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_libros)
    }

    fun visualizarLibro(v : View){
        val intentVisualizar = Intent(this, VisualizarEsteLibroActivity:: class.java).apply {

        }

        startActivity(intentVisualizar)
    }
}