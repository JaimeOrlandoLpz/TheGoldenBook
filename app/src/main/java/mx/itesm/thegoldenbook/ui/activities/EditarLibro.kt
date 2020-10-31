package mx.itesm.thegoldenbook.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_editar_libro.*
import mx.itesm.thegoldenbook.EditarLibroController
import mx.itesm.thegoldenbook.models.Libro
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.interfaces.ClickListener
import mx.itesm.thegoldenbook.ui.adapters.AdaptadorListaLibros

class EditarLibro : AppCompatActivity(), ClickListener {
    private val editarLibroController : EditarLibroController = EditarLibroController()
    lateinit var arrLibros: Array<Libro>
    private val PICK_IMAGE_MULTIPLE  = 1
    lateinit var strImgaeEncode : String
    var lststrImageEncode = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_libro)

        configurarRecycleView()
    }

    private fun configurarRecycleView() {
        val admLayout =LinearLayoutManager(this)

        arrLibros = editarLibroController.getArrLibrosBD("", this)

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

    fun actualizarAdaptador(lsLibros: MutableList<Libro>) {
        val arrLibros = lsLibros.toTypedArray()
        val adaptador= AdaptadorListaLibros(arrLibros)

        rvLibros.adapter = adaptador
        adaptador.notifyDataSetChanged()
        adaptador.listener = this
    }

    fun selectImgsGallery(v: View){
        intent = Intent()
        intent.setType("image/*").putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(Intent.createChooser(intent, "Seleccionar Imágenes"), PICK_IMAGE_MULTIPLE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && data != null){
            var arrstrFilePath : Array<String> = arrayOf( MediaStore.Images.Media._ID)
        }


        super.onActivityResult(requestCode, resultCode, data)
    }
}