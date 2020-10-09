package mx.itesm.Legacy.TheGoldenBook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_crear_libro.*

class CrearLibro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_libro)
    }

    fun grabarDatos(v: View)
    {
        val titulo = etTituloLibro.text.toString()
        val autor = etAutor.text.toString()
        val fecha = etFecha.text.toString()
        subirDatos(titulo, autor, fecha)
    }

    private fun subirDatos(titulo: String, autor: String, fecha: String) {
        val libro = Libro(titulo, autor, fecha)
        val baseDatos = FirebaseDatabase.getInstance()
        val referencia = baseDatos.getReference("/Libro")
        val myRef = referencia.push()
        myRef.setValue(libro)
    }
}