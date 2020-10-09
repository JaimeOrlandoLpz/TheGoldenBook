package mx.itesm.Legacy.TheGoldenBook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class VisualizarLibros : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_libros)
    }

    fun visualizarLibro(v : View){
        val intentVisualizar = Intent(this, VisualizarEsteLibro:: class.java).apply {

        }

        startActivity(intentVisualizar)
    }
}