package mx.itesm.Legacy.TheGoldenBook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_editar_libro.*

class EditarLibro : AppCompatActivity(), ClickListener {
    private val editarLibroController : EditarLibroController = EditarLibroController()
    lateinit var arrLibros: Array<Libro>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_libro)

        configurarRecycleView()
    }

    private fun configurarRecycleView() {
        val admLayout =LinearLayoutManager(this)

        arrLibros = editarLibroController.getArrLibrosBD("")

        val adaptador= AdaptadorListaLibros(arrLibros)

        rvLibros.layoutManager =admLayout
        rvLibros.adapter = adaptador

        adaptador.listener = this
    }

    override fun clicked(posicion: Int) {
        TODO("Not yet implemented")
    }

    fun editarLibro(v : View){
        val intentEditar = Intent(this, EditarEsteLibro:: class.java).apply {

        }

        startActivity(intentEditar)
    }
}