package mx.itesm.Legacy.TheGoldenBook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView


class GaleriaLibros : AppCompatActivity() {
    lateinit var listView: ListView
    var listaLibros: ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        listaLibros.add("Roberto Martínez Román")
        listaLibros.add("Jaime Orlando López")
        listaLibros.add("Gerardo Samuel Sánchez")
        listaLibros.add("José Luis Hernández (Pepe)")
        listaLibros.add("Ricardo Velázquez")
        setContentView(R.layout.activity_galeria_libros)
        super.onCreate(savedInstanceState)

    }


}